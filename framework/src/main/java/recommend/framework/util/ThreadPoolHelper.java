package recommend.framework.util;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.*;

/**
 * @author xiewenwu
 */
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
                10L,
                TimeUnit.MINUTES,
                queue,
                new ThreadFactoryBuilder().setNameFormat("pool-" +name + "-" + "-%d").build(),
                new RejectedExecutionHandler() {
                    @Override
                    public void rejectedExecution(Runnable runnable, ThreadPoolExecutor executor) {
                        runnable.run();
                        log.warn("queue is full, maybe should increase the size of executor {}", executor.toString());
                    }
                });
    }

    public static void close(ThreadPoolExecutor threadPoolExecutor) {
        if (threadPoolExecutor == null) {
            return;
        }

        while(!threadPoolExecutor.getQueue().isEmpty()) {
            log.warn("{} not complete, waiting", threadPoolExecutor.toString());
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        threadPoolExecutor.shutdown();
    }
}
