package recommend.framework.functor;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import recommend.framework.Context;
import recommend.framework.Event;
import recommend.framework.ExpParam;
import recommend.framework.Item;
import recommend.framework.config.Config;
import recommend.framework.config.FunctorConfig;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@Data
@Slf4j
public abstract class AbstractFunctor implements Functor {
    public String type;//算子类型，begin,manager,recall,filter,sort,adjust,resort,position,end,
    public String name;//算子名，SimpleRedis,SimpleFilter,SimpleAdjust,,
    public String metricName;//统计指标名
    public String expName;//实验参数前缀
    public ExpParam expParam;//实验参数包含config
    public Event event;//请求上下文
    public FunctorConfig config;//算子默认配置
    public Context context;//用户上下文
    public Map<String,Object> userFeatures;//用户特征
    public List<Item> items;//物料
    //MetricsReporter metricsReporter;//上报指标

    @Override
    public void open(FunctorConfig config) {
        this.config = config;
        this.name = config.getName();
        //metricsReporter = MetricsReporter.get(type);
        metricName = type + "/" + name;
        expName = type + "_" + name;
    }

    @Override
    public void init() {
        items = event.getItems() != null? event.getItems(): Collections.emptyList();
        context = event.getContext();
        //expParam = new ExpParam(type, name, new Config(context.getExpMap()), new Config(config.config));
        userFeatures = event.getUserFeatures() != null? event.getUserFeatures(): Collections.emptyMap();
    }

    @Override
    public Event invoke(Event event) {
        long startTime = System.currentTimeMillis();
        this.event = event != null? event: Event.EMPTY;

        try {
            init();
            Event tmp = doInvoke(event);

            //metricsReporter.metrics(startTime, tmp.getCode(), tmp.getSize(), getMetricName());
            return tmp;
        } catch (Exception e) {
            //metricsReporter.metrics(startTime, -1, 0, getMetricName());
            log.error("{} {} invoke error:", type, name);
            e.printStackTrace();
        }

        return event;
    }

    @Override
    public void close() {

    }
}
