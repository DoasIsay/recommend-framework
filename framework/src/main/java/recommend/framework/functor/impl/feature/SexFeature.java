package recommend.framework.functor.impl.feature;

/**
 * @author xiewenwu
 */

import recommend.framework.annotation.Functor;
import recommend.framework.functor.AbstractFeature;

@Functor(name = "SexFeature")
public class SexFeature extends AbstractFeature {
    @Override
    public Object get() {
        //return Optional.ofNullable(new RedisPrefixReadWriteTool("userSex_")).map(redis->redis.get(userContext.getUid()));
        return new Object();
    }
}