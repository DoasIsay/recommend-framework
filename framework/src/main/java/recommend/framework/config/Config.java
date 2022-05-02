package recommend.framework.config;


/**
 * @author xiewenwu
 * @date 2022/5/1 16:47
 */

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import recommend.framework.util.JsonHelper;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class Config {
    private JSONObject config;

    public Config() {
    }

    public Config(JSONObject config) {
        this.config = config;
    }

    public Config(String path) {
        config = JsonHelper.fromFile(path);
    }

    public Config(Map<String, Object> config) {
        this.config = new JSONObject(config);
    }

    public Config getConfig(String name) {
        return new Config(getValue(name, new JSONObject()));
    }

    public Map<String, Object> getConfig() {
        return config.getInnerMap();
    }

    public <T> void setValue(String name, T var) {
        config.put(name, var);
    }

    public <T> T getValue(String name) {
        return getValue(name, null);
    }

    public <T> T getValue(String name, T defVar) {
        return (T) config.getOrDefault(name, defVar);
    }

    public int getInt(String name) {
        return getValue(name, 0);
    }

    public int getInt(String key, int defVar) {
        return getValue(key, defVar);
    }

    public float getFloat(String key) {
        return getFloat(key, 0);
    }

    public float getFloat(String key, float defVar) {
        return getValue(key, defVar);
    }

    public String getString(String name) {
        return getValue(name, "");
    }

    public String getString(String key, String defVar) {
        return getValue(key, defVar);
    }

    public <T> List<T> getList(String key, Class<T> classType) {
        return getList(key, classType, null);
    }

    public List<Config> getConfigs(String key) {
        return getConfigs(key, null);
    }

    public List<Config> getConfigs(String key, List<Config> defVar) {
        return Optional.ofNullable(getList(key, JSONObject.class))
                .map(jsonObjects -> jsonObjects.stream().map(Config::new).collect(Collectors.toList()))
                .orElse(defVar);
    }

    public <T> List<T> getList(String key, Class<T> classType, List<T> defVar) {
        Object obj = config.get(key);
        if (obj == null) {
            return defVar;
        }

        if (obj instanceof JSONArray) {
            return ((JSONArray) obj).toJavaList(classType);
        } else if (obj instanceof List) {
            return (List<T>) obj;
        } else {
            return defVar;
        }
    }
    public static void main(String[] argv) {
        Config config = new Config("framework/src/conf.json");
        List<CacheConfig> list = config.getList("conf", CacheConfig.class);
        System.out.println(list.get(0).getDelay());
    }
}

