package recommend.framework.functor;

/**
 * @author xiewenwu
 */

import recommend.framework.Event;
import recommend.framework.Item;
import recommend.framework.config.FunctorConfig;

import java.util.stream.Collectors;

public abstract class AbstractFilter extends AbstractFunctor {
    @Override
    public void open(FunctorConfig config) {
        setType(Type.filter);
        super.open(config);
    }

    public abstract boolean filter(Item item);

    @Override
    public int doInvoke(Event event) {
        int size = event.getSize();
        event.setItems(getItems().stream().filter(item -> !filter(item)).collect(Collectors.toList()));
        if (size != event.getSize()) {
            //todo:记寻过滤日志
            //metricsReporter.counter(size - event.getSize(), "size", getMetricName());
        }
        return 0;
    }
}
