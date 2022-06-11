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
       return new HashMap<String, Object>(4) {{
           put("context", context);
       }};
    }
}
