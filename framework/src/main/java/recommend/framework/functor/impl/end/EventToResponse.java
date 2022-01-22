package recommend.framework.functor.impl.end;

import recommend.framework.Event;
import recommend.framework.functor.AbstractFunctor;

public class EventToResponse extends AbstractFunctor {
    @Override
    public Event doInvoke(Event event) {
        event.setResponse(new Object());
        return event;
    }
}
