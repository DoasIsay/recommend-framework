package recommend.framework.log;

import recommend.framework.Event;
import recommend.framework.util.ThreadPoolHelper;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author xiewenwu
 * @date 2022/5/2 13:09
 */
public class LogManager {
    static final ThreadPoolExecutor threadPool = ThreadPoolHelper.get("LogDumper", 1, 4, 0);
    ConcurrentHashMap<Class, AbstractLog> logMap = new ConcurrentHashMap();
    Event event;

    public LogManager(Event event) {
        this.event = event;
    }

    public <T> T get(Class<T> tClass) {
        return (T) logMap.computeIfAbsent(tClass, k -> {
            try {
                AbstractLog log = (AbstractLog) tClass.newInstance();
                log.init(event);
                return log;
            } catch (Exception e) {
                throw new RuntimeException("get log " + e);
            }
        });
    }

    public void log() {
        logMap.values().forEach(abstractLog -> {
            if (abstractLog.isAsync()) {
                threadPool.submit(abstractLog);
            } else {
                abstractLog.run();
            }
        });
    }
}
