package recommend.framework.functor.impl.adjust;

/**
 * @author xiewenwu
 */

import recommend.framework.Item;
import recommend.framework.annotation.Functor;
import recommend.framework.functor.AbstractAdjust;
import recommend.framework.util.StringHelper;

import java.util.Set;

@Functor(name = "ChnAdjust")
public class ChnAdjust extends AbstractAdjust {
    public Set<String> chn;

    @Override
    public void init() {
        super.init();
        //对指定的召回通道召回主播进行调权
        chn = StringHelper.toSet(expParam.getValue("chn", ""));
        weight = expParam.getFloat("weight", 5.5f);
    }

    @Override
    public void adjust(Item item) {
        if (chn.contains(item.getChn())) {
            item.setAdjQ(item.getAdjQ() + weight);
        }
    }
}
