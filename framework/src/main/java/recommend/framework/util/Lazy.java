package recommend.framework.util;

import java.util.function.Supplier;

/**
 * @author xiewenwu
 */
public class Lazy<T> {
    T instance;
    Supplier<T> supplier;

    public Lazy(Supplier<T> supplier) {
        this.supplier = supplier;
    }

    public T get() {
        if (instance != null) {
            return instance;
        }

        synchronized (supplier) {
            if (instance == null) {
                if (supplier != null) {
                    instance = supplier.get();
                }
            }
        }

        return instance;
    }
}
