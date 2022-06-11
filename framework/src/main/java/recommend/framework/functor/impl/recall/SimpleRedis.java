package recommend.framework.functor.impl.recall;

/**
 * @author xiewenwu
 */

import recommend.framework.Item;
import recommend.framework.annotation.Functor;
import recommend.framework.functor.AbstractRecall;

import java.util.ArrayList;
import java.util.List;

@Functor(name = "SimpleRedis")
public class SimpleRedis extends AbstractRecall {
    @Override
    public List<Item> recall() {
        return new ArrayList<Item>(){{
            add(new Item("465456", 1f));
            add(new Item("46547", 2f));
            add(new Item("46548", 3f));
            add(new Item("46549", 4f));
        }};
        /*
        return Optional.ofNullable(jedis)
                .map(redis -> redis.get(expParam.getString("prefix", null)+"_"+context.getHdid()))
                .map(str -> Arrays.stream(str.split(","))
                        .map(aidScore -> {
                            String[] fields = aidScore.split(":");
                            if (fields.length==2) {
                                return new Item(fields[0], NumberUtils.toFloat(fields[1], 0f));
                            }
                            return null;
                        }).filter(item -> item != null)
                        .collect(Collectors.toList())
                ).orElse(Collections.emptyList());
                 */
    }
}
