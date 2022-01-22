package recommend.framework.functor.impl.manager;

import recommend.framework.annotation.Functor;
import recommend.framework.config.Config;
import recommend.framework.functor.AbstractManager;

@Functor(name = "position")
public class PositionManager extends AbstractManager {
    @Override
    public void open(Config config) {
        setType("position");
        setMode(Mode.serial);
        super.open(config);
    }
}
