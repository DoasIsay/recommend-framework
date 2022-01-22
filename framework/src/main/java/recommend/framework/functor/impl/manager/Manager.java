package recommend.framework.functor.impl.manager;

import recommend.framework.Event;
import recommend.framework.annotation.Functor;
import recommend.framework.config.Config;
import recommend.framework.functor.AbstractManager;

@Functor(name = "manager")
public class Manager extends AbstractManager {
    public Manager(Config config) {
        setType("manager");
        setMode(Mode.serial);
        super.open(config);
    }

    @Override
    public Event doInvoke(Event event) {
        //BeginManager->FeatureManager->RecallManager->FilterManager->SortManager->ResortManager->PositionManager->EndManager.....
        getFunctors().forEach(manager -> manager.invoke(event));
        return event;
    }
}
