package recommend.framework.functor;


/**
 * @author xiewenwu
 */

import recommend.framework.Event;
import recommend.framework.Item;
import recommend.framework.config.FunctorConfig;

import java.util.List;

public abstract class AbstractResort extends AbstractFunctor {
    @Override
    public void open(FunctorConfig config) {
        setType(Type.resort);
        super.open(config);
    }

    public abstract List<Item> resort(List<Item> items);

    @Override
    public int doInvoke(Event event) {
        event.setItems(resort(getItems()));
        return 0;
    }
}
