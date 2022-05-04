package recommend.framework.util;

import recommend.framework.MyClassLoader;
import recommend.framework.annotation.Functor;
import recommend.framework.functor.impl.sort.SimpleSort;

import java.io.File;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * @author xiewenwu
 * @date 2022/5/4 18:15
 */
public class JarHelper {
    static public Class loadJAR(String jarPath, String className) {
        File jarFile = new File(jarPath);
        if (!jarFile.exists()) {
            return null;
        }

        Class cls = null;
        try {
            URL url = jarFile.toURI().toURL();
            URLClassLoader classLoader = new URLClassLoader(new URL[]{url}, new MyClassLoader());
            cls = classLoader.loadClass(className);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return cls;
    }

    static public Map<String, Class<?>> loadJAR(String jarFilePath, String packagePath, Class c) {
        System.out.println(jarFilePath);
        File jarFile = new File(jarFilePath);
        if (!jarFile.exists()) {
            return null;
        }

        List<String> classNames = null;
        try {
            classNames = getJarFiles(jarFilePath, packagePath);
        } catch (IOException e) {
            e.printStackTrace();
        }

        Map<String, Class<?>> classMap = new HashMap<>();
        try {
            URL url = jarFile.toURI().toURL();
            URLClassLoader classLoader = new URLClassLoader(new URL[]{url}, new MyClassLoader());
            for (String className : classNames) {
                Class classType = classLoader.loadClass(className);
                Annotation annotation = classType.getDeclaredAnnotation(c);
                if (annotation == null) {
                    continue;
                }
                Method typeMethod = c.getMethod("type");
                Method nameMethod = c.getMethod("name");
                String name = (String) nameMethod.invoke(annotation);
                String type = (String) typeMethod.invoke(annotation);
                if (name.isEmpty()) {
                    name = classType.getSimpleName();
                }
                //name = getName(classType, name);无需考虑不同type算子重名问题，强制各类算子命名带上类型，如SimpleRecall,SimpleFilter,SimpleAdjust
                System.out.println(jarFile.getPath() + " get annotation: " + name + "\tclassType: " + classType.getName() + "\tclassLoader: " + classType.getClassLoader().getClass().getName());
                Class tmp = classMap.get(name);
                if (tmp != null) {
                    System.out.println("functor name: " + name + " has tow classType old class: " + classType.getName() + " new class: " + tmp.getName() + " please check, now exit");
                    System.exit(-1);
                }
                classMap.put(name, classType);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return classMap;
    }

    static public List<String> getJarFiles(String jarFilePath, String packagePath) throws IOException {
        List<String> files = new ArrayList<>();
        ZipFile zipFile = null;
        try {
            zipFile = new ZipFile(jarFilePath);
            Enumeration<? extends ZipEntry> iterator = zipFile.entries();
            while (iterator.hasMoreElements()) {
                ZipEntry entry = iterator.nextElement();
                String name = entry.getName().replace("/", ".");
                if (!name.endsWith("class") || !name.startsWith(packagePath)) {
                    continue;
                }
                files.add(name.substring(0, name.lastIndexOf(".")));
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            zipFile.close();
        }

        return files;
    }

    public static void main(String[] argv) {

        System.out.println(new SimpleSort().getClass().getClassLoader());
        Map<String, Class<?>> classMap = JarHelper.loadJAR("F:\\xiewenwu\\recommend-framework\\target\\recommend-framework-1.0-SNAPSHOT-jar-with-dependencies1.jar", "recommend",  Functor.class);
        System.out.println(classMap);
        System.out.println(classMap.get("SimpleSort").getClassLoader());
    }
}
