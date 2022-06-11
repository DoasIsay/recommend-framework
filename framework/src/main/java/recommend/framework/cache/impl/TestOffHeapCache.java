package recommend.framework.cache.impl;

import recommend.framework.cache.AbstractOffHeapCache;

/**
 * @author xiewenwu
 */
public class TestOffHeapCache extends AbstractOffHeapCache<String, String> {
    @Override
    public void update() {

    }

    @Override
    public int getSize() {
        return 0;
    }

    @Override
    public <T> T getCache() {
        return null;
    }

    public static void main(String[] argv) {
        TestOffHeapCache cache = new TestOffHeapCache();
        long startTime = System.currentTimeMillis();
        for (int i = 0 ;i < 100000; i++) {
            String x = String.valueOf(i);
            cache.offHeapCache.put(x, x);
        }
        long stopTime = System.currentTimeMillis();
        System.out.println((stopTime-startTime));

        System.out.println(cache.offHeapCache.get("1"));
    }
}
