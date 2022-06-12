package recommend.framework.functor;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.ctrip.framework.apollo.ConfigFile;
import com.ctrip.framework.apollo.core.enums.ConfigFileFormat;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import recommend.framework.Event;
import recommend.framework.config.Config;
import recommend.framework.config.ConfigManager;
import recommend.framework.config.FunctorConfig;
import recommend.framework.util.AnnotationHelper;
import recommend.framework.util.FileMonitor;
import recommend.framework.util.JarHelper;
import recommend.framework.util.JsonHelper;

import java.io.File;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author xiewenwu01
 */

@Slf4j
public class FunctorFactory {
    static volatile Map<String, Class<?>> name2Class = AnnotationHelper.getAnnotationClass("recommend.framework.functor", recommend.framework.annotation.Functor.class);

    @Getter
    static volatile Map<String, FunctorConfig> name2Config = Collections.emptyMap();

    static volatile List<FunctorConfig> functors;

    static FileMonitor fileMonitor;

    static {
        ConfigManager.register("functor", ConfigFileFormat.JSON, FunctorFactory::load);
        fileMonitor = new FileMonitor("F:\\xiewenwu\\recommend-framework\\target\\", (String path)->path.endsWith(".jar"), (File file)->load(file));
    }

    public FunctorFactory() {
    }

    public static Config load(ConfigFile configFile) {
        Config config;
        if (configFile != null) {
            config = new Config(JsonHelper.fromString(configFile.getContent()));
        } else {
            config = new Config("framework/src/conf.json");
        }

        System.out.println(config);
        functors = config.getList("functors", FunctorConfig.class);
        name2Config = functors.stream()
                .filter(functorConfig -> StringUtils.isNotEmpty(functorConfig.getName()))

                .collect(Collectors.toMap(FunctorConfig::getName, v -> v));
        System.out.println(JSON.toJSONString(name2Config, SerializerFeature.PrettyFormat));
        log.info(JSON.toJSONString(name2Config, SerializerFeature.PrettyFormat));
        return config;
    }


    private static void load(File file) {
        try {
            Map<String, Class<?>> tmp = JarHelper.loadJAR("file:/"+file.getAbsolutePath(), recommend.framework.annotation.Functor.class);
            name2Class = tmp;
            Functor functor = create("SimpleSort");
            functor.invoke(new Event());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Functor create(String name) {
        Class c = name2Class.get(name);
        if (c == null) {
            throw new RuntimeException("not support: " + name);
        }

        try {
            return Functor.class.cast(c.newInstance());
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("create instance fail: " + name);
        }
    }

    public static FunctorConfig getConfig(String name) {
        return name2Config.get(name);
    }

    public static void close() {
        fileMonitor.close();
    }
}
