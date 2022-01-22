package recommend.framework.annotation;

import lombok.extern.slf4j.Slf4j;
import org.reflections.Reflections;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Slf4j
public class AnnotationHelper {
    public static Set<String> annotationClass = new HashSet<>();

    public static Map<String, Class<?>> getAnnotationClass(Class c, String ...paths) {
        Map<String, Class<?>> classMap = new HashMap<>();
        for (String path: paths) {
            classMap.putAll(getAnnotationClass(path, c));
        }
        return classMap;
    }

    public static Map<String, Class<?>> getAnnotationClass(String path, Class c) {
        Reflections reflections = new Reflections(path);
        Set<Class<?>> classSet = reflections.getTypesAnnotatedWith(c);

        Map<String, Class<?>> classMap = new HashMap<>();
        classSet.forEach(classType -> {
            try {
                Method typeMethod = c.getMethod("type");
                Method nameMethod = c.getMethod("name");
                Annotation annotation = classType.getDeclaredAnnotation(c);
                String name = (String) nameMethod.invoke(annotation);
                String type = (String) typeMethod.invoke(annotation);
                if (name.isEmpty())
                    name = classType.getSimpleName();
                //name = getName(classType, name);无需考虑不同type算子重名问题，强制各类算子命名带上类型，如SimpleRecall,SimpleFilter,SimpleAdjust
                System.out.println(path + " get annotation: " + name + "\tclassType: " + classType.getName());
                Class tmp = classMap.get(name);
                if (tmp != null) {
                    System.out.println("functor name: " + name + " has tow classType old class: " + classType.getName() + " new class: "+ tmp.getName() + " please check, now exit");
                    System.exit(-1);
                }
                classMap.put(name, classType);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        annotationClass.addAll(classMap.keySet());
        return classMap;
    }

    public static <T> T getInstance(String name, String type, Map<String, Class<?>> map) {
        return getInstance(name+"_"+type, map);
    }

    public static <T> T getInstance(String name, Map<String, Class<?>> map) {
        Class<T> c = (Class<T>) map.get(name);
        if (c == null)
            throw new RuntimeException("not support: " + name);
        try {
            return (T) c.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("create instance fail: " + name);
        }
    }

    public static void main(String[] args) {
        getAnnotationClass("recommend.frame", Functor.class);
    }
}
