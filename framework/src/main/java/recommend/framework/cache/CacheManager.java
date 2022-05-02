package recommend.framework.cache;

import com.ctrip.framework.apollo.ConfigFile;
import com.ctrip.framework.apollo.core.enums.ConfigFileFormat;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import recommend.framework.util.AnnotationHelper;
import recommend.framework.config.CacheConfig;
import recommend.framework.config.Config;
import recommend.framework.config.ConfigManager;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author xiewenwu
 * @date 2022/3/22 16:29
 */

@Slf4j
public class CacheManager {
    static volatile boolean first = true;
    static Map<String, AbstractCache> cacheMap = new ConcurrentHashMap<>();
    static Map<String, Class<?>> classMap;
    List<CacheConfig> lastVersionCacheConfigs = Collections.emptyList();

    @Getter
    static CacheManager instance = new CacheManager();

    public CacheManager() {
        ConfigManager.register("cache", ConfigFileFormat.JSON, this::load);
        classMap = AnnotationHelper.getAnnotationClass("recommend.framework.cache.impl", recommend.framework.annotation.Cache.class);
        first = false;
    }

    Config load(ConfigFile configFile) {
        Config config = ConfigManager.toConfig(configFile);

        List<CacheConfig> cacheConfigs = config.getList("conf", CacheConfig.class);
        cacheConfigs.forEach(cacheConfig -> {
            if (!checkConfig(cacheConfig))
                return;

            try {
                String name = cacheConfig.getValue("name");
                AbstractCache cache = AnnotationHelper.getInstance(name, classMap);

                cache.init(cacheConfig);
                AbstractCache old = cacheMap.put(name, cache);
                if (old != null)
                    old.stop();
            } catch (Exception e) {
                log.error("init cache {} error: ", cacheConfig.getName(), e);
            }
        });

        lastVersionCacheConfigs = cacheConfigs;
        return config;
    }

    boolean checkConfig(CacheConfig cacheConfig) {
        if (first)
            return true;

        //不考虑删除的情况，禁止删除线上正在使用的cache
        CacheConfig lastVersionCacheConfig = lastVersionCacheConfigs.stream().filter(config -> config.getName().equals(cacheConfig.getName())).findAny().orElse(null);
        if (lastVersionCacheConfig != null) {
            //版本号改变reload
            if (lastVersionCacheConfig.getVersion() != cacheConfig.getVersion()) {

                return true;
            } else {
                return false;
            }
        }
        //新增
        return true;
    }

    static public <T> T get(String name, Class<T> c) {
        return (T) cacheMap.get(name);
    }

    public static boolean update() {//确保处理数据前cache已load
        log.info("check cache start");

        while(true) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            break;
        }

        return true;
    }

    static {
        update();
    }
}
