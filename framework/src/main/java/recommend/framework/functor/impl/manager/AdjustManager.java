package recommend.framework.functor.impl.manager;

import recommend.framework.annotation.Functor;
import recommend.framework.config.Config;
import recommend.framework.functor.AbstractManager;

@Functor(name = "adjust")
public class AdjustManager extends AbstractManager {
    @Override
    public void open(Config config) {
        setType("adjust");
        setMode(Mode.serial);
        super.open(config);
    }
}
