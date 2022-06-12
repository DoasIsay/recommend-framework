package recommend.framework.functor;

/**
 * @author xiewenwu
 */

import recommend.framework.Event;
import recommend.framework.Item;
import recommend.framework.config.FunctorConfig;

import java.util.List;

public abstract class AbstractSort extends AbstractFunctor{
    @Override
    public void open(FunctorConfig config) {
        setType(Type.sort);
        super.open(config);
    }

    public abstract List<Item> sort(List<Item> items);

    @Override
    public int doInvoke(Event event) {
        List<Item> tmp = sort(getItems());
        tmp.forEach(item -> {
            item.setAdjQ(item.getSortQ());
        });

        event.setItems(tmp);
        return 0;
    }
}
