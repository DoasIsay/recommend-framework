package recommend.framework.functor;

/**
 * @author xiewenwu
 */

import org.apache.commons.lang.StringUtils;
import recommend.framework.Event;
import recommend.framework.config.FunctorConfig;

import java.util.Map;
import java.util.Optional;

public abstract class AbstractTag extends AbstractFunctor {
    @Override
    public void open(FunctorConfig config) {
        setType(Type.tag);
        super.open(config);
    }

    public abstract String get();

    @Override
    public int doInvoke(Event event) {
        String tag = get();
        if (StringUtils.isNotEmpty(tag)) {
            Optional.ofNullable((Map)event.getUserFeature("tag", null))
            .ifPresent(map -> map.put(tag, true));
        }
        //todo:记录实时特征日志？
        return 0;
    }
}
