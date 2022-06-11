package recommend.framework.functor.impl.recall;

/**
 * @author xiewenwu
 */

import org.apache.commons.lang.math.NumberUtils;
import org.apache.commons.math3.util.Pair;
import recommend.framework.Item;
import recommend.framework.annotation.Functor;
import recommend.framework.functor.AbstractRecall;
import recommend.framework.util.StringHelper;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Functor(name = "PosRecall")
public class PosRecall extends AbstractRecall {
    //强运营占位召回，从接口获取占位及主播，产品占位可在占位类中通过召回通道名占位
    Set<Integer> posSet;
    @Override
    public void init() {
        super.init();
        posSet = StringHelper.toIntSet(expParam.getString("pos",""));
    }

    @Override
    public List<Item> recall() {
        //业务后台传来占位信息
        Map<Integer, String> posId = StringHelper.toMap("1:123466,2:45678,6:5677855",",",":", k -> NumberUtils.toInt(k,0), v->(v));
        Map<Integer, Item> posItem = posId.entrySet().stream().map(entry->new Pair<>(entry.getKey(), new Item(entry.getValue(),0f))).collect(Collectors.toMap(Pair::getKey, Pair::getValue));

        event.setValue(getName(), posItem);
        return posItem.values().stream().collect(Collectors.toList());
    }
}
