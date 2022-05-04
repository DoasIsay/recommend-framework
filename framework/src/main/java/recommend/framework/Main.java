package recommend.framework;

import recommend.framework.functor.FunctorFactory;
import recommend.framework.functor.impl.manager.Manager;

public class Main {
    public static void main(String[] argv) {
        new Manager(FunctorFactory.getName2Config().get("MainManager")).invoke(new Event());
        return;
    }
}
