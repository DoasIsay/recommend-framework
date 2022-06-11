package recommend.framework.cache;

/**
 * @author xiewenwu
 */
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import recommend.framework.config.CacheConfig;
import recommend.framework.metrics.impl.MetricsReporter;

import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public abstract class AbstractCache implements Runnable {
    private static final Logger LOG = LoggerFactory.getLogger(AbstractCache.class);
    private static final MetricsReporter metricsReporter = MetricsReporter.get("cache");
    private static final ScheduledExecutorService scheduleExecutor = Executors.newScheduledThreadPool(8
            , new ThreadFactoryBuilder().setPriority(3).setNameFormat("AsyncCache-%d").build());

    public boolean isLog = true;
    public CacheConfig config;
    Future future;

    public void init(CacheConfig config) {
        this.config = config;
        future = scheduleExecutor.scheduleWithFixedDelay(this::run, config.getDelay(), config.getPeriod(), TimeUnit.SECONDS);
    }

    public boolean isUpdate() { return getSize() > 0;}
    public abstract void update();

    public abstract int getSize();
    public abstract <T> T getCache();

    @Override
    public void run() {
        long startTime = System.currentTimeMillis();
        String name = config.getName();

        try {
            update();
            metricsReporter.metrics(startTime, 0, getSize(), name);
            if (Math.random() < 0.1 && isLog) {
                LOG.info("cache {} {}", name, getSize());
            }
        } catch (Exception e) {
            e.printStackTrace();
            metricsReporter.metrics(startTime, -1, 0, name);
        }
    }

    public void stop() {
        future.cancel(true);
    }
}