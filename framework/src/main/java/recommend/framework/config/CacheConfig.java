package recommend.framework.config;

/**
 * @author xiewenwu
 */

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CacheConfig extends Config {
    public CacheConfig(String name, int delay, int period) {
        this.setName(name);
        this.setDelay(delay);
        this.setPeriod(period);
    }

    String name;
    int delay = 1;
    int period = 1;
    int version = 0;
}
