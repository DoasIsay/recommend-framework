package recommend.framework.functor.impl.resort;

/**
 * @author xiewenwu
 */

import recommend.framework.Item;
import recommend.framework.annotation.Functor;
import recommend.framework.functor.AbstractResort;

import java.util.Collections;
import java.util.List;

@Functor(name = "SimpleResort")
public class SimpleResort extends AbstractResort {
    @Override
    public List<Item> resort(List<Item> items) {
        Collections.shuffle(items);
        return items;
    }
}
