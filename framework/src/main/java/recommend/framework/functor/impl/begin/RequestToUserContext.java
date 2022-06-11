package recommend.framework.functor.impl.begin;

/**
 * @author xiewenwu
 */

import recommend.framework.Context;
import recommend.framework.Event;
import recommend.framework.annotation.Functor;
import recommend.framework.functor.AbstractFunctor;

@Functor(name = "RequestToUserContext")
public class RequestToUserContext extends AbstractFunctor {
    @Override
    public int doInvoke(Event event) {
        event.setContext(new Context());
        return 0;
    }
}
