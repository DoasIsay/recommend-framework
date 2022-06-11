package recommend.framework.cache;


import org.mapdb.DBMaker;
import org.mapdb.HTreeMap;

import java.util.concurrent.TimeUnit;

/**
 * @author xiewenwu
 */
public abstract class AbstractOffHeapCache<K,V> extends AbstractCache {
    public HTreeMap<K, V> offHeapCache;
    public AbstractOffHeapCache() {
        offHeapCache = getMapDBCache();
    }

    public HTreeMap<K, V> getMapDBCache(){
        String name = this.getClass().getSimpleName();
            return (HTreeMap<K, V>) DBMaker
                    .fileDB("framework/src/"+name+".db")
                    .fileMmapEnableIfSupported()
                    .fileChannelEnable()
                    .closeOnJvmShutdown()
                    .concurrencyScale(32)
                    .make()
                    .hashMap(name)
                    .expireMaxSize(10000)
                    .expireAfterCreate(10, TimeUnit.SECONDS)
                    .expireAfterUpdate(10, TimeUnit.SECONDS)
                    .expireAfterGet(10, TimeUnit.SECONDS)
                    .createOrOpen();
    }
}
