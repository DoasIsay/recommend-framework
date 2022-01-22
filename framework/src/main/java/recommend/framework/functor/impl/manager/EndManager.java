package recommend.framework.functor.impl.manager;

import recommend.framework.annotation.Functor;
import recommend.framework.config.Config;
import recommend.framework.functor.AbstractManager;

@Functor(name = "end")
public class EndManager extends AbstractManager {
    @Override
    public void open(Config config) {
        setType("end");
        setMode(Mode.serial);
        super.open(config);
    }
}
