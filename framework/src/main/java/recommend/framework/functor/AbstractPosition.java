package recommend.framework.functor;

/**
 * @author xiewenwu
 */

import lombok.extern.slf4j.Slf4j;
import recommend.framework.Event;
import recommend.framework.Item;
import recommend.framework.config.FunctorConfig;

import java.util.*;

@Slf4j
public abstract class AbstractPosition extends AbstractFunctor{
    public List<Item> result;
    Set<Item> dup;

    @Override
    public void open(FunctorConfig config) {
        setType(Type.position);
        super.open(config);
    }

    public boolean isDup(Item item) {
        return dup.contains(item);
    }

    public boolean isEmpty(int pos) {
        return result.get(pos).isEmpty();
    }

    public int getPos(int pos) {
        int relativePos = pos - 1 - context.getOffset();
        //位置越界
        if (relativePos < 0 || relativePos > context.getSize()) {
            log.error("pos {} not in range 0-{}", relativePos, context.getSize());
            return -1;
        }

        if (!isEmpty(relativePos)) {
            return -1;
        }

        return relativePos;
    }

    public void insertItem(int pos, Item item) {
        result.set(pos, item);
        dup.add(item);
    }

    public List<Item> getResult(int size) {
        List<Item> tmp = event.getResult();
        if (tmp == null) {
            tmp = new ArrayList<>(Collections.nCopies(size, Item.EMPTY));
            event.setResult(tmp);
        }
        return tmp;
    }

    public abstract void arrange(List<Item> items);

    @Override
    public int doInvoke(Event event) {
        result = getResult(context.getSize());
        dup = new HashSet<>(result);
        System.out.println(result);
        System.out.println(getItems());
        //运营强占位，产品高优先级预先占位
        arrange(getItems());
        return 0;
    }
}
