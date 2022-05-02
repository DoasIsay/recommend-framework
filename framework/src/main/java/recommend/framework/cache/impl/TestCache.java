package recommend.framework.cache.impl;

import recommend.framework.annotation.Cache;
import recommend.framework.cache.AbstractCache;

import java.util.ArrayList;
import java.util.List;

/**
 * @author xiewenwu
 * @date 2022/4/7 19:47
 */

@Cache
public class TestCache extends AbstractCache {
    private volatile List<Double> cache;

    @Override
    public void update() {
        List<Double> tmp = new ArrayList<>(10);
        for (int i = 0; i< 10; i++) {
            cache.add(Math.random());
        }
        cache = tmp;
    }

    @Override
    public int getSize() {
        return cache.size();
    }

    @Override
    public List<Double> getCache() {
        return cache;
    }
}
