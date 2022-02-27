package recommend.framework;

import recommend.framework.config.Config;
import recommend.framework.functor.FunctorFactory;
import recommend.framework.functor.impl.manager.Manager;

public class Main {
    public static Config config = new Config();

    public static void main(String[] argv) {
        config.reload(null);
        FunctorFactory.setName2Config(config.getName2Config());
        new Manager(config.getName2Config().get("MainManager")).invoke(new Event());
    }
}
