package recommend.framework.functor.impl.manager;

import lombok.extern.slf4j.Slf4j;
import recommend.framework.annotation.Functor;
import recommend.framework.config.Config;
import recommend.framework.config.FunctorConfig;
import recommend.framework.functor.AbstractManager;

@Slf4j
@Functor(name = "feature")
public class FeatureManager extends AbstractManager {
    @Override
    public void open(FunctorConfig config) {
        setType("feature");
        setMode(Mode.parallel);
        super.open(config);
    }
}
