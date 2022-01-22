package recommend.framework.functor.impl.position;

import recommend.framework.Item;
import recommend.framework.annotation.Functor;
import recommend.framework.functor.AbstractPosition;

import java.util.List;

@Functor(name = "SimplePosition")
public class SimplePosition extends AbstractPosition {
    @Override
    public List<Item> arrange(List<Item> items) {
        return items;
    }
}
