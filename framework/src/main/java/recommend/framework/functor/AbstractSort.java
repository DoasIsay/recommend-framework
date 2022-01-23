package recommend.framework.functor;

import recommend.framework.Event;
import recommend.framework.Item;
import recommend.framework.config.Config;
import recommend.framework.config.FunctorConfig;

import java.util.List;

public abstract class AbstractSort extends AbstractFunctor{
    @Override
    public void open(FunctorConfig config) {
        setType("sort");
        super.open(config);
    }

    public abstract List<Item> sort(List<Item> items);

    @Override
    public Event doInvoke(Event event) {
        event.setItems(sort(getItems()));
        return event;
    }
}
