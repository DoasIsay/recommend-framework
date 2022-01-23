package recommend.framework.functor;


import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import recommend.framework.Main;
import recommend.framework.annotation.AnnotationHelper;
import recommend.framework.config.Config;
import recommend.framework.config.FunctorConfig;

import java.io.File;
import java.util.Collections;
import java.util.Map;

@Data
@Slf4j
public class FunctorFactory {
    public static volatile Map<String, Class<?>> classMap = AnnotationHelper.getAnnotationClass("recommend.framework.functor", recommend.framework.annotation.Functor.class);
    volatile Map<String, FunctorConfig> name2Config = Collections.emptyMap();
    public static Config config;
    public FunctorFactory() {
        //new FileMonitor("lib/functor", (String path)->path.endsWith(".jar"), (File file)->reload(file));
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
        FunctorConfig config = Main.config.getName2Config().get(name);
        if (config == null) {
            log.error("functor config not found error: {}", name);
            System.out.println("null "+name);
            return null;
        }

        Functor functor = AnnotationHelper.getInstance(config.getFunctor(), classMap);
        functor.open(config);
        return functor;
    }
}
