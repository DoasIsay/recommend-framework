package recommend.framework.util;

import lombok.extern.slf4j.Slf4j;
import org.reflections.Reflections;
import recommend.framework.annotation.Functor;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @author xiewenwu
 */
@Slf4j
public class AnnotationHelper {
    public static Set<String> annotationClass = new HashSet<>();

    public static String getAnnotationType(Class target, Class ann) {
        return getAnnotationValue(target, ann, "type");
    }

    public static String getAnnotationName(Class target, Class ann) {
        String name = getAnnotationValue(target, ann, "name");
        if (name != null && name.isEmpty()) {
            return target.getSimpleName();
        }

        return name;
    }

    public static <T> T getAnnotationValue(Class target, Class ann, String field) {
        try {
            Annotation annotation = target.getDeclaredAnnotation(ann);
            if (annotation == null) {
                return null;
            }
            Method method = ann.getMethod(field);
            return (T) method.invoke(annotation);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public static Map<String, Class<?>> getAnnotationClass(Class c, String ...paths) {
        Map<String, Class<?>> classMap = new HashMap<>();
        for (String path: paths) {
            classMap.putAll(getAnnotationClass(path, c));
        }
        return classMap;
    }

    public static Map<String, Class<?>> getAnnotationClass(String path, Class ann) {
        Reflections reflections = new Reflections(path);
        Set<Class<?>> classSet = reflections.getTypesAnnotatedWith(ann);

        Map<String, Class<?>> classMap = new HashMap<>();
        classSet.forEach(classType -> {
                String name = getAnnotationName(classType, ann);
                if (name == null) {
                    return;
                }
                //String type = getAnnotationType(classType, ann);无需考虑不同type算子重名问题，强制各类算子命名带上类型，如SimpleRecall,SimpleFilter,SimpleAdjust
                System.out.println(path + " get annotation: " + name + "\tclassType: " + classType.getName());
                Class tmp = classMap.get(name);
                if (tmp != null) {
                    System.out.println("functor name: " + name + " has tow classType old class: " + classType.getName() + " new class: "+ tmp.getName() + " please check, now exit");
                    System.exit(-1);
                }
                classMap.put(name, classType);
        });
        annotationClass.addAll(classMap.keySet());
        return classMap;
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
