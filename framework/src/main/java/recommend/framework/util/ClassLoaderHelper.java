package recommend.framework.util;

import lombok.extern.slf4j.Slf4j;

import java.net.URL;
import java.net.URLClassLoader;

/**
 * @author xiewenwu
 */

@Slf4j
public class ClassLoaderHelper extends URLClassLoader {
    public ClassLoaderHelper(URL url) {
        this(new URL[] { url }, Thread.currentThread().getContextClassLoader());
    }

    public ClassLoaderHelper(URL[] urls, ClassLoader parent) {
        super(urls, parent);
    }

    @Override
    protected Class<?> loadClass(String className, boolean resolve) throws ClassNotFoundException {
        Class<?> clazz= findClass(className);
        if (clazz != null) {
            System.out.println("load class name: " + className + " by " + clazz.getClassLoader());
            return clazz;
        }

        ClassLoader parent = getParent();
        if(parent != null) {
            clazz = parent.loadClass(className);
        }

        if (clazz == null) {
            throw new ClassNotFoundException(className);
        }

        System.out.println("load class name: " + className + " by " + clazz.getClassLoader());
        if (resolve) {
            resolveClass(clazz);
        }

        return clazz;
    }

    @Override
    protected Class<?> findClass(String className) {
        Class<?> clazz = findLoadedClass(className);
        if (clazz != null) {
            return clazz;
        }

        try {
            clazz = super.findClass(className);
        } catch (ClassNotFoundException e) {
        }

        return clazz;
    }
}
