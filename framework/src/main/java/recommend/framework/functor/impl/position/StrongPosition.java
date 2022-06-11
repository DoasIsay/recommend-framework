package recommend.framework.functor.impl.position;

/**
 * @author xiewenwu
 */

import recommend.framework.Item;
import recommend.framework.annotation.Functor;
import recommend.framework.functor.AbstractPosition;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@Functor(name = "StrongPosition")
public class StrongPosition extends AbstractPosition {
    String chn;

    @Override
    public void init() {
        super.init();
        chn = expParam.getString("chn");
    }

    //运营强占位，由PosRecall从接口获取占位及主播信息
    @Override
    public void arrange(List<Item> items) {
        Map<Integer, Item> posItem =  event.getValue(chn, Collections.emptyMap());

        posItem.entrySet().forEach(entry -> {
            int relativePos = getPos(entry.getKey());
            if (relativePos < 0) {
                return;
            }

            Item item = entry.getValue();
            if (isDup(item)) {
                return;
            }

            insertItem(relativePos, item);
        });
    }
}
