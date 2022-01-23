package recommend.framework.functor.impl.manager;

import recommend.framework.config.Config;
import recommend.framework.config.FunctorConfig;
import recommend.framework.functor.AbstractManager;

import java.util.List;

@recommend.framework.annotation.Functor(name = "filter")
public class FilterManager extends AbstractManager {
    List<recommend.framework.functor.Functor> filterFunctors;
    public void open(FunctorConfig config) {
        setType("filter");
        setMode(Mode.serial);
        super.open(config);
        filterFunctors = getFunctors();
    }
}
