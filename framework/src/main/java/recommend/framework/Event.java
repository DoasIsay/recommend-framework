package recommend.framework;

import lombok.Data;
import recommend.framework.log.LogManager;
import recommend.framework.util.ExtInfo;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Data
public class Event extends ExtInfo {
    public static final Event EMPTY = new Event();
    long eventTime = System.currentTimeMillis();

    String id;//请求唯一标识

    Object request;//用户请求
    public void setRequest(Object request) {
        this.request = request;
    }

    public <T> T getRequest() {
        return (T) request;
    }

    Object response;//返回结果
    public void setResponse(Object response) {
        this.response = response;
    }

    public <T> T getResponse() {
        return (T) response;
    }

    Context context;//用户上下文
    public void setContext(Context context) {
        this.context = context;
    }

    public <T> T getContext() {
        return (T) context;
    }

    public <T> T getContext(Class<T> tClass) {
        return (T) context;
    }

    Map<String, Object> userFeatures = new ConcurrentHashMap<>();//用户特征存map方便规则引擎使用
    List<Item> items = Collections.emptyList();//物料集
    int code;//0返回为空，1成功，-1失败，-2超时，-3任务取消
    int size;//items大小

    public void setItems(List<Item> items) {
        this.items = items;
        if (items == null) code = -1;
        size = items.size();
        code = size == 0 ? 0 : 1;
    }

    public <T> void setUserFeature(Class c, T var) {
        setUserFeature(c.getSimpleName(), var);
    }

    public <T> void setUserFeature(String name, T var) {
        if (var instanceof Map) {
            userFeatures.putAll((Map) var);
        }
        userFeatures.put(name, var);
    }

    public <T> T getUserFeature(Class c, T defVar) {
        return getUserFeature(c.getSimpleName(), defVar);
    }

    public <T> T getUserFeature(String name, T defVar) {
        return (T) userFeatures.getOrDefault(name, defVar);
    }

    LogManager logManager = new LogManager(this);
}
