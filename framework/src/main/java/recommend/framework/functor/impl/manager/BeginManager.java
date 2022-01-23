package recommend.framework.functor.impl.manager;

import recommend.framework.annotation.Functor;
import recommend.framework.config.Config;
import recommend.framework.config.FunctorConfig;
import recommend.framework.functor.AbstractManager;

@Functor(name = "begin")
public class BeginManager extends AbstractManager {
    @Override
    public void open(FunctorConfig config) {
        setType("begin");
        setMode(Mode.serial);
        super.open(config);
    }
}
