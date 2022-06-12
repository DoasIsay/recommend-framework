package recommend.framework;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SimplePropertyPreFilter;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import recommend.framework.cache.CacheManager;
import recommend.framework.config.ConfigManager;
import recommend.framework.config.FunctorConfig;
import recommend.framework.functor.AbstractManager;
import recommend.framework.functor.FunctorFactory;
import recommend.framework.functor.impl.manager.Manager;
import recommend.framework.log.LogManager;
import recommend.framework.util.KafkaHelper;

import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author xiewenwu
 */
@Slf4j
public class Main {
    public static void main(String[] argv) {
        System.out.println("start manager");

        FunctorConfig config = FunctorFactory.getName2Config().get("MainManager");
        Event event = new Event();
        new Manager(config).invoke(event);

        System.out.println("stop manager");

        Gson gson = new Gson();
        System.out.println("recall: " + gson.toJson(event.getItems()));
        System.out.println("result: " + gson.toJson(event.getResult()));
        System.out.println("tags: "   + gson.toJson(event.getTags()));

        close();
        return;
    }

    static void close() {
        AbstractManager.threadPoolMap.values().forEach(ThreadPoolExecutor::shutdown);
        LogManager.close();
        KafkaHelper.close();
        CacheManager.close();
        FunctorFactory.close();
    }
}
