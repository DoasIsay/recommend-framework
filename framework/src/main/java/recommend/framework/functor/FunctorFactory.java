package recommend.framework.functor;


import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import recommend.framework.util.AnnotationHelper;
import recommend.framework.config.FunctorConfig;

import java.io.File;
import java.util.Collections;
import java.util.Map;

@Slf4j
public class FunctorFactory {
    public static volatile Map<String, Class<?>> name2Class = AnnotationHelper.getAnnotationClass("recommend.framework.functor", recommend.framework.annotation.Functor.class);
    @Setter
    static volatile Map<String, FunctorConfig> name2Config = Collections.emptyMap();

    public FunctorFactory() {
        //new FileMonitor("lib/functor", (String path)->path.endsWith(".jar"), (File file)->reload(file));
    }

    private static void reload(File file) {
        try {
            ClassLoader.getSystemClassLoader().loadClass(file.getAbsolutePath());
            Map<String, Class<?>> tmp = AnnotationHelper.getAnnotationClass("functor.impl", Functor.class);
            name2Class = tmp;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static Functor get(String name) {
        return create(name);
    }

    public static Functor create(String name) {
        FunctorConfig functorConfig = name2Config.get(name);
        if (functorConfig == null) {
            log.error("functor appConfig not found error: {}", name);
            System.out.println("functor appConfig not found error: " + name);
            return null;
        }
        Class c = name2Class.get(functorConfig.getFunctor());
        if (c == null)
            throw new RuntimeException("not support: " + name);

        Functor functor;
        try {
            functor = (Functor) c.newInstance();
            functor.open(functorConfig);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("create instance fail: " + name);
        }

        return functor;
    }
}
