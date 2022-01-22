package recommend.framework.functor;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import recommend.framework.Event;
import recommend.framework.Item;
import recommend.framework.config.Config;
import recommend.framework.rulengine.RuleEngineFactory;
import recommend.framework.util.ThreadPoolHelper;

import java.util.*;
import java.util.concurrent.*;

@Data
@Slf4j
public abstract class AbstractManager extends AbstractFunctor {
    public enum Mode {
        serial,   //串行执行，比如过滤，调权算子串行
        cutout,   //短路执行，遍历算子任何一个执行成功就结束，先出现的算子优先级高（占位算子有优先级），后边算子优先级低可做兜底（观星排序失败，快排兜底）
        parallel  //并行执行，比如获取特征，多路召回算子是并行的
    }
    Mode mode = Mode.serial;//manager管理的算子运行模式
    public int timeout;
    static Map<String, ThreadPoolExecutor> threadPoolMap = new ConcurrentHashMap<>();
    public ThreadPoolExecutor threadPool;

    @Override
    public void open(Config config) {
        super.open(config);
        timeout = config.getValue("timeout", 60);
        threadPool = ThreadPoolHelper.get(getType() + getName(), 8, 64, 0);
    }

    public boolean strategyFilter(String name) {
        //实验参数开关
        String openParamName = type+"_"+name+ ".open";
        //简单规则表达式，比如圏用户
        String expressParamName = type+"_"+name + ".express";

        boolean flag = Optional.ofNullable(expParam.getString(openParamName, "0"))
                .map(open -> "1".equals(open) || "true".equals(open))
                .orElse(false);
        //未命中实验
        if (!flag) return false;

        if (userFeatures == null || userFeatures.isEmpty())
            return true;
        //通过用户相关属性来判断是否开启此策略，比如，信息流用户，新户，高活，，
        return (Boolean) Optional.ofNullable(expParam.getString(expressParamName, ""))
                .filter(StringUtils::isNotEmpty)
                .map(expressStr -> RuleEngineFactory.get(expParam.getString("ruleEngine", "aviator")).execute(expressStr, userFeatures))
                .orElse(true);
    }

    public List<Functor> getFunctors() {
        return getFunctors(false);
    }

    public List<Functor> getFunctors(boolean async) { //todo: filter并发初始化？无必要，一般从redis拉数据耗时在2ms左右，而最长的召回耗时在20-30ms
        String functorNames = config.getString(type);
        if (StringUtils.isEmpty(functorNames)) {
            log.warn("not find {} config", type);
            return Collections.emptyList();
        }

        List<Functor> functors = new ArrayList<>();
        for (String functorName : functorNames.split(",")) {
            if (!strategyFilter(functorName)) continue;
            Functor functor = FunctorFactory.get(functorName);
            if (functor != null) functors.add(functor);
        }
        return functors;
    }

    @Override
    public Event doInvoke(Event event) {
        switch (mode) {
            case serial: return serialInvoke(event);
            case cutout: return cutoutInvoke(event);
            case parallel: return parallelInvoke(event);
            default: throw new RuntimeException("not support mode: " + mode);
        }
    }

    Event parallelInvoke(Event event) {
        List<Callable<Event>> featureTasks = new ArrayList<>();
        getFunctors().forEach(functor -> featureTasks.add(()->functor.invoke(event)));

        List<Item> result = new ArrayList<>();
        try {
            List<Future<Event>> futures = threadPool.invokeAll(featureTasks, timeout, TimeUnit.MILLISECONDS);
            for (Future<Event> future: futures) {
                if (future.isDone()) {
                    Event tmp = future.get();
                    result.addAll(tmp.getItems());
                } else {
                    log.error("{} {} threadPool invokerAll error: task is cancel", type, name);
                }
            }
            event.setItems(result);
        } catch (InterruptedException | ExecutionException e) {
            event.setCode(-1);
            log.error("{} {} threadPool invokerAll error:", type, name);
        }
        return event;
    }

    Event serialInvoke(Event event) {
        getFunctors().forEach(functor -> functor.invoke(event));
        return event;
    }

    Event cutoutInvoke(Event event) {
        getFunctors().forEach(functor -> {
            functor.invoke(event);
            if (event.getCode() > 0) return;
        });
        return event;
    }

    @Override
    public void close() {

    }
}

