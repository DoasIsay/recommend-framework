package recommend.framework.functor;

import recommend.framework.Event;
import recommend.framework.Item;
import recommend.framework.config.Config;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractPosition extends AbstractFunctor{
    public List<Item> emptyList;

    @Override
    public void open(Config config) {
        setType("position");
        super.open(config);
    }

    public void setItem(int pos, Item item) {
        int realPos = pos - context.getOffset();
        if (emptyList.get(realPos).isEmpty())
        emptyList.set(realPos, item);
    }

    public List<Item> getEmptyItems(int size) {
        List<Item> emptyList = new ArrayList<>(size);
        for (int idx = 0; idx < size; ++idx) {
            emptyList.set(idx, Item.EMPTY);
        }
        return emptyList;
    }

    public abstract List<Item> arrange(List<Item> items);

    @Override
    public Event doInvoke(Event event) {
        emptyList = getEmptyItems(context.getExpectNum());
        //运营强占位，产品高优先级预先占位
        arrange(getItems());

        //从items中选取占剩余的空位
        int idx= 0;
        for (Item item: emptyList) {
            if (!item.isEmpty()) continue;
            emptyList.set(idx, items.get(idx++));
        }

        event.setItems(emptyList);
        return event;
    }
}
