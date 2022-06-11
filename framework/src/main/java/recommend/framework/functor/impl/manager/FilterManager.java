package recommend.framework.functor.impl.manager;

/**
 * @author xiewenwu
 */

import recommend.framework.functor.Functor;
import recommend.framework.config.FunctorConfig;
import recommend.framework.functor.AbstractManager;

import java.util.List;

@recommend.framework.annotation.Functor(name = "filter")
public class FilterManager extends AbstractManager {
    List<Functor> filterFunctors;
    @Override
    public void open(FunctorConfig config) {
        super.open(config);
        filterFunctors = getFunctors();
    }
}
