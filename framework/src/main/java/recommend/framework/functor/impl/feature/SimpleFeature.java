package recommend.framework.functor.impl.feature;

/**
 * @author xiewenwu
 */

import recommend.framework.annotation.Functor;
import recommend.framework.functor.AbstractFeature;

import java.util.HashMap;

@Functor(name = "SimpleFeature")
public class SimpleFeature extends AbstractFeature {
    @Override
    public Object get() {
        return new HashMap<String, Object>() {{
            put("offset",   context.getOffset());
            put("version",  context.getVersion());
            put("city",     context.getCity());
            put("os",       context.getOs());
        }};
    }
}
