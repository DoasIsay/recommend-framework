package recommend.framework;

import recommend.framework.config.Config;

public class ExpParam {
    String ns;
    String type;
    String name;
    Config exp;
    Config def;

    public ExpParam(String type, String name, Config exp, Config def) {
        this.type = type;
        this.name = name;
        this.exp = exp;
        this.def = def;
        this.ns = type + "_" + name + ".";
    }

    String getKey(String key) {
        return ns + key;
    }

    public <T> T getValue(String key, T defVar) {
        //优先从实验平台拿参数，拿不到使用配置的默认参数
        return exp.getValue(getKey(key), def.getValue(key, defVar));
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
    public String getString(String key, String defVar) {
        return getValue(key, defVar);
    }
}
