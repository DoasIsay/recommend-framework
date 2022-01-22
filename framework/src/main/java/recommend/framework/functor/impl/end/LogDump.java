package recommend.framework.functor.impl.end;

import recommend.framework.Event;
import recommend.framework.functor.AbstractFunctor;

public class LogDump extends AbstractFunctor {
    @Override
    public Event doInvoke(Event event) {
        //context.getLogManager().log();
        return event;
    }
}
