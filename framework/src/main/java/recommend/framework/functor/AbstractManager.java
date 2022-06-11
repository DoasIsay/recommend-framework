package recommend.framework.functor;

/**
 * @author xiewenwu
 */

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import recommend.framework.Event;
import recommend.framework.ExpParam;
import recommend.framework.config.Config;
import recommend.framework.config.FunctorConfig;
import recommend.framework.rulengine.RuleEngineFactory;
import recommend.framework.util.ThreadPoolHelper;

import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Data
@Slf4j
public abstract class AbstractManager extends AbstractFunctor {
    //manager管理的算子运行模式
    Mode mode = Mode.serial;
    public int timeout;
    static Map<String, ThreadPoolExecutor> threadPoolMap = new ConcurrentHashMap<>();
    public ThreadPoolExecutor threadPool;

    @Override
    public void open(FunctorConfig config) {
        setType(Type.manager);
        super.open(config);
        timeout = config.getValue("timeout", 60);
        mode = Mode.valueOf(config.getString("mode", Mode.serial.name()));
        threadPool = ThreadPoolHelper.get(getType() + "-" + getName(), 8, 64, 0);
    }

    public boolean strategyFilter(ExpParam expParam) {
        //实验参数开关
        boolean flag = Optional.ofNullable(expParam)
                .map(param -> param.getValue("open", false))
                .orElse(false);

        //未命中实验
        if (!flag) {
            return true;
        }

        if (userFeatures == null || userFeatures.isEmpty()) {
            return false;
        }

        //简单规则表达式，比如圏用户通过用户相关属性来判断是否开启此策略，比如新户，高活，，
        return Optional.ofNullable(expParam.getString("express", ""))
                .filter(StringUtils::isNotEmpty)
                .map(expressStr -> !(boolean) RuleEngineFactory.get(expParam.getString("ruleEngine", "aviator")).execute(expressStr, userFeatures))
                .orElse(false);
    }

    public List<Functor> getFunctors() {
        List<String> functorNames = config.getFunctors();
        if (functorNames == null || functorNames.isEmpty()) {
            log.warn("{} functors is empty", config.getName());
            return Collections.emptyList();
        }

        return functorNames.parallelStream().map(name -> {
            FunctorConfig functorConfig = FunctorFactory.getConfig(name);
            if (functorConfig == null) {
                return null;
            }

            //构造算子参数
            ExpParam expParam = new ExpParam(
                    functorConfig.getType(),
                    functorConfig.getName(),
                    context != null ? context.getExpConfig() : Config.EMPTY,
                    new Config(functorConfig.getConfig())
            );

            //todo: 落盘生效参数
            if (strategyFilter(expParam)) {
                return null;
            }

            System.out.println(expParam);
            AbstractFunctor functor = (AbstractFunctor) FunctorFactory.create(functorConfig.getFunctor());
            if (functor != null) {
                functor.open(functorConfig);
                functor.setExpParam(expParam);
            }

            return functor;
        }).filter(functor -> functor != null).collect(Collectors.toList());
    }

    @Override
    public int doInvoke(Event event) {
        switch (mode) {
            case serial:
                return doInvokeSerial(event);
            case cutoff:
                return doInvokeCutOff(event);
            case parallel:
                return doInvokeParallel(event);
        }
        return 0;
    }

    public int doInvokeParallel(Event event) {
        List<Functor> functors = getFunctors();
        if (functors.isEmpty()) {
            return 0;
        }

        List<Callable<Integer>> featureTasks = new ArrayList<>(functors.size());
        functors.forEach(functor -> featureTasks.add(() -> functor.invoke(event)));

        try {
            threadPool.invokeAll(featureTasks, timeout, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            log.error("{} {} threadPool invokerAll error:", type, name, e);
        }
        return 0;
    }

    public int doInvokeSerial(Event event) {
        getFunctors().forEach(functor -> functor.invoke(event));
        return 0;
    }

    public int doInvokeCutOff(Event event) {
        getFunctors().forEach(functor -> {
            if (functor.invoke(event) >= 0) {
                return;
            }
        });
        return 0;
    }

    @Override
    public void close() {

    }

    public enum Mode {
        //串行执行，比如过滤，调权算子
        serial,
        //短路执行，遍历算子任何一个执行成功就结束，先出现的算子优先级高（占位算子有优先级），后边算子优先级低可做兜底（观星排序失败，快排兜底）
        cutoff,
        //并行执行，比如获取特征，多路召回算子是并行的
        parallel
    }
}

