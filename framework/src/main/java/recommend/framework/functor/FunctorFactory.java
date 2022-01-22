package recommend.framework.functor;

import lombok.extern.slf4j.Slf4j;
import recommend.framework.annotation.AnnotationHelper;
import recommend.framework.config.Config;
import recommend.framework.util.FileMonitor;

import java.io.File;
import java.util.Collections;
import java.util.Map;

@Slf4j
public class FunctorFactory {
    static volatile Map<String, Class<?>> classMap = AnnotationHelper.getAnnotationClass("functor.impl", Functor.class);
    volatile Map<String, Config> nameConfigMap = Collections.emptyMap();

    private FunctorFactory() {
        new FileMonitor("lib/functor", (String path)->path.endsWith(".jar"), (File file)->reload(file));
    }

    private static void reload(File file) {
        try {
            ClassLoader.getSystemClassLoader().loadClass(file.getAbsolutePath());
            Map<String, Class<?>> tmp = AnnotationHelper.getAnnotationClass("functor.impl", Functor.class);
            classMap = tmp;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private static FunctorFactory instance = new FunctorFactory();

    public static Functor get(String name) {
        return instance.create(name);
    }

    public Functor create(String name) {
        Config config = nameConfigMap.get(name);
        if (config == null) {
            log.error("not find {} config", name);
            return null;
        }
        String className = config.getString("class");
        Functor functor = AnnotationHelper.getInstance(className, classMap);
        functor.open(config);
        functor.setName(name);
        return functor;
    }
}
