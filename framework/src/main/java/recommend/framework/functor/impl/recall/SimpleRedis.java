package recommend.framework.functor.impl.recall;

import org.apache.commons.lang.math.NumberUtils;
import recommend.framework.Item;
import recommend.framework.annotation.Functor;
import recommend.framework.functor.AbstractRecall;
import redis.clients.jedis.Jedis;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Functor(name = "SimpleRedis")
public class SimpleRedis extends AbstractRecall {
    Jedis jedis;
    @Override
    public void init() {
        super.init();
        jedis = new Jedis();
    }

    @Override
    public List<Item> recall() {
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
    }
}
