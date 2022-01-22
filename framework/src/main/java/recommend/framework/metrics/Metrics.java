package recommend.framework.metrics;

public interface Metrics {
    void metrics(long start, int code, int size, String interfaceName);
    Metrics get(String type);
}
