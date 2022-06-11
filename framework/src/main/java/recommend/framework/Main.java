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
        System.out.println("start manager");

        Event event = new Event();
        new Manager(FunctorFactory.getName2Config().get("MainManager")).invoke(event);

        System.out.println("stop manager");
        System.out.println("recall: " + event.getItems());
        System.out.println("result: " + event.getResult());
        System.out.println("tags:" + event.getTags());

        return;
    }
}
