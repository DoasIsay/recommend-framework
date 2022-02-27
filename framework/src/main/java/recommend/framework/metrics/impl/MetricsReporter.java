package recommend.framework.metrics.impl;

import recommend.framework.metrics.Metrics;

import java.util.concurrent.ConcurrentHashMap;

public class MetricsReporter implements Metrics {
    private static final ConcurrentHashMap<String, MetricsReporter> metricsReporterMap = new ConcurrentHashMap<>();

    @Override
    public void metrics(long start, int code, int size, String interfaceName) {

    }

    public static MetricsReporter get(String type) {
        return metricsReporterMap.computeIfAbsent(type, key -> new MetricsReporter());
    }
}
