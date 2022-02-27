package recommend.framework.functor.impl.position;

import recommend.framework.Item;
import recommend.framework.annotation.Functor;
import recommend.framework.functor.AbstractPosition;
import recommend.framework.util.StringHelper;

import java.util.List;
import java.util.Map;
import java.util.Set;

@Functor(name = "ChnPosition")
public class ChnPosition extends AbstractPosition {
    private Map<String, Set<Integer>> chnPos;
    @Override
    public void init() {
        //目前产品需求一般会新开发召回从池子召回，比如开发HotGuardRecall要占5,6,7号位，在此占位类中可把5,6,7号位用HotGuardRecall召回的主播填充
        super.init();
        //HotGuardRecall:5,6,7|PosRecall:4,5,6
        chnPos = StringHelper.toMap(expParam.getString("chnPos", ""), ";", ":", k->(k), v->(StringHelper.toIntSet(v)));
    }
    @Override
    public List<Item> arrange(List<Item> items) {
        items.forEach(item -> {
            Set<Integer> posSet = chnPos.get(item.getChn());
            if (posSet == null || posSet.isEmpty()) return;
            Integer pos = posSet.iterator().next();
            setItem(pos , item);
            posSet.remove(pos);
        });
        return null;
    }
}
