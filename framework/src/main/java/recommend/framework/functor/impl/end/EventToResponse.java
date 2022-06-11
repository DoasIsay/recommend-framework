package recommend.framework.functor.impl.end;

/**
 * @author xiewenwu
 */

import recommend.framework.Event;
import recommend.framework.annotation.Functor;
import recommend.framework.functor.AbstractFunctor;

@Functor
public class EventToResponse extends AbstractFunctor {
    @Override
    public int doInvoke(Event event) {
        event.setResponse(event.getResult());
        return 0;
    }
}
