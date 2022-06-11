package recommend.framework.functor.impl.position;

/**
 * @author xiewenwu
 */

import recommend.framework.Item;
import recommend.framework.annotation.Functor;
import recommend.framework.functor.AbstractPosition;

import java.util.List;

@Functor(name = "SimplePosition")
public class SimplePosition extends AbstractPosition {
    //优先级最低占剩余的空位
    @Override
    public void arrange(List<Item> items) {
        int pos = 0;
        int idx = 0;
        while(pos < result.size() && idx < items.size()) {
            Item item = items.get(idx);
            //位置已被占
            if (!isEmpty(pos)) {
                pos++;
                continue;
            }

            //物料重复
            if (isDup(item)) {
                ++idx;
                continue;
            }

            insertItem(pos, item);
            pos++;
            idx++;
        }
    }
}
