package recommend.framework.util;

import org.apache.commons.io.FileUtils;
import recommend.framework.Event;
import recommend.framework.annotation.Functor;
import recommend.framework.config.FunctorConfig;
import recommend.framework.functor.impl.sort.SimpleSort;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.*;
import java.util.jar.JarFile;
import java.util.stream.Collectors;

/**
 * @author xiewenwu
 */
public class JarHelper {
    static public Map<String, Class<?>> loadJAR(String jarFilePath, Class ann) {
        URL url = null;
        List<String> classNames = null;
        try {
            url = new URL(jarFilePath);
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return null;
        }

        try(JarFile jarFile = getJarFile(url)) {
            classNames = getClassName(jarFile);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        Map<String, Class<?>> classMap = new HashMap<>();
        URLClassLoader classLoader = new ClassLoaderHelper(url);
        for (String className : classNames) {
            Class classType;
            try {
                classType = classLoader.loadClass(className);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
                continue;
            }

            String name = AnnotationHelper.getAnnotationName(classType, ann);
            if (name == null) {
                continue;
            }
            //String type = getAnnotationType(classType, ann);无需考虑不同type算子重名问题，强制各类算子命名带上类型，如SimpleRecall,SimpleFilter,SimpleAdjust
            System.out.println(jarFilePath + " get annotation: " + name + "\tclassType: " + classType.getName());
            Class tmp = classMap.get(name);
            if (tmp != null) {
                System.out.println("functor name: " + name + " has tow classType old class: " + classType.getName() + " new class: "+ tmp.getName() + " please check, now exit");
                System.exit(-1);
            }
            classMap.put(name, classType);
        }

        return classMap;
    }

    static JarFile getJarFile(URL url) throws IOException {
        String filePath = url.getPath().substring(1);
        if ("file".equals(url.getProtocol())) {
            return new JarFile(filePath);
        }

        FileUtils.copyURLToFile(url, new File(filePath), 1000, 1000);
        return new JarFile(filePath);
    }

    static public List<String> getClassName(JarFile jarFile) {
        return jarFile.stream()
                .filter(entry -> entry.getName().endsWith("class"))
                .map(entry -> {
                    String name = entry.getName();
                    return name.replace("/", ".").substring(0, name.lastIndexOf("."));
                })
                .collect(Collectors.toList());
    }

    public static void main(String[] argv) throws IllegalAccessException, InstantiationException, ClassNotFoundException, NoSuchMethodException, InvocationTargetException, MalformedURLException {

        Map<String, Class<?>> classMap = JarHelper.loadJAR("file:/F:\\xiewenwu\\recommend-framework\\recommend-framework-1.0-SNAPSHOT-jar-with-dependencies2.jar", Functor.class);
        System.out.println(classMap);

        System.out.println(new SimpleSort().getClass().getClassLoader());
        System.out.println(classMap.get("SimpleSort").getClassLoader());

        Class c = classMap.get("SimpleSort");
        recommend.framework.functor.Functor functor = recommend.framework.functor.Functor.class.cast(c.newInstance());
        functor.open(new FunctorConfig());
        functor.invoke(new Event());
        new SimpleSort().sort(Collections.emptyList());
        System.out.println(SimpleSort.class.getClassLoader());
        System.out.println(new SimpleSort().getClass().getClassLoader());
        System.out.println(ClassLoader.getSystemClassLoader());
    }
}
