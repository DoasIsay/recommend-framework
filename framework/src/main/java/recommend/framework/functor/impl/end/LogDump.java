package recommend.framework.functor.impl.end;

import recommend.framework.Event;
import recommend.framework.annotation.Functor;
import recommend.framework.functor.AbstractFunctor;
import recommend.framework.log.impl.LogRequest;

@Functor
public class LogDump extends AbstractFunctor {
    @Override
    public Event doInvoke(Event event) {
        event.getLogManager().get(LogRequest.class);
        event.getLogManager().log();
        return event;
    }
}
