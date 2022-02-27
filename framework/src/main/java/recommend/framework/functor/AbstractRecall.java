package recommend.framework.functor;

import lombok.Data;
import recommend.framework.Event;
import recommend.framework.Item;
import recommend.framework.config.FunctorConfig;
import recommend.framework.functor.impl.manager.FilterManager;

import java.util.List;

@Data
public abstract class AbstractRecall extends AbstractFunctor {
    @Override
    public void open(FunctorConfig config) {
        setType("recall");
        super.open(config);
    }

    private FilterManager filterManager;

    public abstract List<Item> recall();

    @Override
    public Event doInvoke(Event event) {
        List<Item> items = recall();
        items.forEach(item -> item.setChn(getName()));
        event.setItems(items);
        //统一过滤
        //filterManager.invoke(event);
        //召回截断
        int exceptNum = expParam.getValue("exceptNum", 100);
        event.setItems(event.getSize() > exceptNum? event.getItems().subList(0, exceptNum): event.getItems());

        if (event.getSize() != 0) {
            //todo:记录召回日志
        }

        //metricsReporter.counter(event.getSize(), "size", getMetricName());
        return event;
    }
}
