package recommend.framework.util;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.*;

@Slf4j
public class ThreadPoolHelper {
    public static ThreadPoolExecutor get(String name, int min, int max, int queueSize) {
        BlockingQueue<Runnable> queue;
        if (queueSize != 0) {
            queue = new ArrayBlockingQueue<>(queueSize);
        } else {
            queue = new SynchronousQueue();
        }

        return new ThreadPoolExecutor(
                min,
                max,
                10l,
                TimeUnit.MINUTES,
                queue,
                new ThreadFactoryBuilder().setNameFormat("pool-" +name + "-" + "-%d").build(),
                new RejectedExecutionHandler() {
                    public void rejectedExecution(Runnable runnable, ThreadPoolExecutor executor) {
                        runnable.run();
                        log.warn("queue is full, maybe should increase the size of executor {}", executor.toString());
                    }
                });
    }
}
