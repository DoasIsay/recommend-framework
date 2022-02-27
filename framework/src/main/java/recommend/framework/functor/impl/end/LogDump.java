package recommend.framework.functor.impl.end;

import recommend.framework.Event;
import recommend.framework.annotation.Functor;
import recommend.framework.functor.AbstractFunctor;

@Functor
public class LogDump extends AbstractFunctor {
    @Override
    public Event doInvoke(Event event) {
        //context.getLogManager().log();
        return event;
    }
}
