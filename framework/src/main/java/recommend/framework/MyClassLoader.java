package recommend.framework;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author xiewenwu
 * @date 2022/5/4 18:20
 */
public class MyClassLoader extends ClassLoader {
    public Class<?> loadClass(File file) throws IOException {
        byte b[] = new byte[(int) file.length()];
        FileInputStream fis = null;
        Class<?> clazz = null;

        try {
            fis = new FileInputStream(file);
            int j = 0;
            while (true) {
                int i = fis.read(b);
                if (i == -1) {
                    break;
                }

                j += i;
            }
            clazz = super.defineClass(null, b, 0, j);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            fis.close();
        }

        return clazz;
    }

    @Override
    public Class<?> loadClass(String name) throws ClassNotFoundException {
        try {
            InputStream is = getClass().getResourceAsStream(name);
            if (is == null) {
                System.out.println("null");
            }

            byte[] b = new byte[is.available()];

            is.read(b);
            return super.defineClass(name, b, 0, b.length);

        } catch (IOException e) {
            e.printStackTrace();
            throw new ClassNotFoundException(name);
        }
    }
}
