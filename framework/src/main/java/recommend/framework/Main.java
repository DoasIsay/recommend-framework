package recommend.framework;

import recommend.framework.config.AppConfig;
import recommend.framework.functor.FunctorFactory;
import recommend.framework.functor.impl.manager.Manager;

public class Main {
    public static AppConfig appConfig = new AppConfig();

    public static void main(String[] argv) {
        appConfig.load(null);
        FunctorFactory.setName2Config(appConfig.getName2Config());

        new Manager(appConfig.getName2Config().get("MainManager")).invoke(new Event());
        return;
    }
}
