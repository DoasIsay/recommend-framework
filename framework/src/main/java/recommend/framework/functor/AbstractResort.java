package recommend.framework.functor;

import recommend.framework.Event;
import recommend.framework.Item;
import recommend.framework.config.Config;

import java.util.List;

public abstract class AbstractResort extends AbstractFunctor{
    @Override
    public void open(Config config) {
        setType("resort");
        super.open(config);
    }

    public abstract List<Item> resort(List<Item> items);

    @Override
    public Event doInvoke(Event event) {
        event.setItems(resort(getItems()));
        return event;
    }
}
