package recommend.framework.functor.impl.adjust;

import recommend.framework.Item;
import recommend.framework.annotation.Functor;
import recommend.framework.functor.AbstractAdjust;
import recommend.framework.util.StringHelper;

import java.util.Set;

@Functor(name = "ChnAdjust")
public class ChnAdjust extends AbstractAdjust {
    public Set<String> chn;
    public float weight;

    @Override
    public void init() {
        super.init();
        //对指定的召回通道召回主播进行调权
        chn = StringHelper.toSet(expParam.getValue("chn", ""));
        weight = expParam.getFloat("weight", 5.5f);
    }

    @Override
    public Item adjust(Item item) {
        if (chn.contains(item.getChn())) {
            item.setScore(item.getScore() + weight);
        }
        return item;
    }
}
