package recommend.framework.config;

/**
 * @author xiewenwu
 */

import lombok.Data;
import recommend.framework.util.ExtInfo;

import java.util.List;

@Data
public class FunctorConfig extends ExtInfo {
    String type;
    String name;
    String functor;
    List<String> functors;
}
