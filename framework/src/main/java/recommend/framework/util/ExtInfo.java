package recommend.framework.util;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

/**
 * @author xiewenwu
 * 通用扩展字段基类
 */
public class ExtInfo {
    @Getter
    public transient Map<String, Object> config = new HashMap<>();
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
    public float getFloat(String key) { return getFloat(key, 0);}
    public float getFloat(String key, float defVar) { return getValue(key, defVar); }
    public String getString(String name) { return getValue(name, ""); }
    public String getString(String key, String defVar) { return getValue(key, defVar); }
}
