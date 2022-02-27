package recommend.framework.functor.impl.manager;

import recommend.framework.annotation.Functor;
import recommend.framework.config.FunctorConfig;
import recommend.framework.functor.AbstractManager;

@Functor(name = "Manager")
public class Manager extends AbstractManager {
    public Manager() {}
    public Manager(FunctorConfig config) {
        super.open(config);
    }
}
