package recommend.framework.functor.impl.position;

import recommend.framework.Item;
import recommend.framework.functor.AbstractPosition;

import java.util.Collections;
import java.util.List;
import java.util.Map;

public class StrongPosition extends AbstractPosition {
    //运营强占位，由PosRecall从接口获取占位及主播信息
    @Override
    public List<Item> arrange(List<Item> items) {
        Map<Integer, String> posId =  event.getValue("PosRecall", Collections.emptyMap());

        posId.entrySet().forEach(entry -> setItem(entry.getKey(), new Item(entry.getValue(),0f)));

        return null;
    }
}
