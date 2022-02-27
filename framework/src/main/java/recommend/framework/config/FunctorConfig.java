package recommend.framework.config;

import lombok.Data;
import recommend.framework.util.ExtInfo;

@Data
public class FunctorConfig extends ExtInfo {
    public String name;
    public String functor;
    public String functors;
}
