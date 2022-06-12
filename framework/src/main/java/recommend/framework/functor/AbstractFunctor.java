package recommend.framework.functor;

/**
 * @author xiewenwu
 */

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import recommend.framework.Context;
import recommend.framework.Event;
import recommend.framework.ExpParam;
import recommend.framework.Item;
import recommend.framework.config.FunctorConfig;
import recommend.framework.metrics.impl.MetricsReporter;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@Data
@Slf4j
public abstract class AbstractFunctor implements Functor {
    public Type type = Type.functor;//算子类型，begin,manager,recall,filter,sort,adjust,resort,position,end,
    public String name;//算子名，SimpleRedis,SimpleFilter,SimpleAdjust,,
    public String metricName;//统计指标名
    public ExpParam expParam;//实验参数包含config
    public Event event;//请求上下文
    public FunctorConfig config;//算子默认配置
    public Context context;//用户上下文
    public Map<String,Object> userFeatures;//用户特征
    public List<Item> items;//物料
    MetricsReporter metricsReporter;//上报指标

    @Override
    public void open(FunctorConfig config) {
        this.config = config;
        this.name = config.getName();
        metricsReporter = MetricsReporter.get(type.name());
        metricName = type + "/" + name;
    }

    @Override
    public void init() {
        items = event.getItems() != null? event.getItems(): Collections.emptyList();
        context = event.getContext();
        userFeatures = event.getUserFeatures() != null? event.getUserFeatures(): Collections.emptyMap();
    }

    @Override
    public int invoke(Event event) {
        long startTime = System.currentTimeMillis();
        this.event = event != null? event: Event.EMPTY;
        System.out.println(getType()+"-"+getName()+ " run");

        //0成功，1返回为空，-1失败
        int code = 0;
        try {
            init();
            code = doInvoke(event);
            metricsReporter.metrics(startTime, code, event.getSize(), getMetricName());

            //System.out.println(event.getItems());
        } catch (Exception e) {
            code = -1;
            metricsReporter.metrics(startTime, code, 0, getMetricName());
            e.printStackTrace();
            log.error("{} {} invoke error:", type, name);
            e.printStackTrace();
            code = -1;
        }
        return code;
    }

    @Override
    public void close() {

    }

    enum Type {
        functor,
        manager,
        begin,
        feature,
        tag,
        recall,
        filter,
        sort,
        adjust,
        resort,
        position,
        end
    }
}
