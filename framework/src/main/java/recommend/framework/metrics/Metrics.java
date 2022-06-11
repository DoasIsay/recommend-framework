package recommend.framework.metrics;
/**
 * @author xiewenwu
 */
public interface Metrics {
    void metrics(long start, int code, int size, String interfaceName);
}
