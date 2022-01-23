package recommend.framework.functor.impl.manager;

import recommend.framework.annotation.Functor;
import recommend.framework.config.Config;
import recommend.framework.config.FunctorConfig;
import recommend.framework.functor.AbstractManager;

@Functor(name = "sort")
public class SortManager extends AbstractManager {
    @Override
    public void open(FunctorConfig config) {
        setType("sort");
        setMode(Mode.cutout);
        super.open(config);
    }
}
