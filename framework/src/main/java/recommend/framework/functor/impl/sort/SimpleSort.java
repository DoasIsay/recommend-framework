package recommend.framework.functor.impl.sort;

/**
 * @author xiewenwu
 */

import recommend.framework.Item;
import recommend.framework.annotation.Functor;
import recommend.framework.functor.AbstractSort;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Functor(name = "SimpleSort")
public class SimpleSort extends AbstractSort {
    @Override
    public List<Item> sort(List<Item> items) {
        System.out.println(getName() + " " + this.getClass().getClassLoader());
        items.forEach(item -> {
            item.setSortQ((float) Math.random());
        });

        Collections.sort(items, Comparator.comparing(Item::getOrigQ,Comparator.reverseOrder()));
        return items;
    }
}
