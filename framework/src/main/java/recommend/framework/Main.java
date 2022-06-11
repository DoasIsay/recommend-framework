package recommend.framework;

import lombok.extern.slf4j.Slf4j;
import recommend.framework.functor.FunctorFactory;
import recommend.framework.functor.impl.manager.Manager;

/**
 * @author xiewenwu
 */
@Slf4j
public class Main {
    public static void main(String[] argv) {
        log.info("start");
        Event event = new Event();
        new Manager(FunctorFactory.getName2Config().get("MainManager")).invoke(event);
        System.out.println(event.getItems());
        System.out.println(event.getResult());
        return;
    }
}
