package recommend.framework.functor.impl.manager;

import recommend.framework.annotation.Functor;
import recommend.framework.config.Config;
import recommend.framework.functor.AbstractManager;

@Functor(name = "resort")
public class ResortManager extends AbstractManager {
    @Override
    public void open(Config config) {
        setType("resort");
        setMode(Mode.cutout);
        super.open(config);
    }
}