package recommend.framework.functor;

import recommend.framework.Event;
import recommend.framework.Item;
import recommend.framework.config.Config;

import java.util.stream.Collectors;

public abstract class AbstractFilter extends AbstractFunctor {
    @Override
    public void open(Config config) {
        setType("filter");
        super.open(config);
    }

    public abstract boolean filter(Item item);

    @Override
    public Event doInvoke(Event event) {
        int size = event.getSize();
        event.setItems(getItems().stream().filter(item -> !filter(item)).collect(Collectors.toList()));
        if (size != event.getSize()) {
            //todo:记寻过滤日志
            //metricsReporter.counter(size - event.getSize(), "size", getMetricName());
        }
        return event;
    }
}
