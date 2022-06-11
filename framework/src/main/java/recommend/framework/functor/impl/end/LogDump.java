package recommend.framework.functor.impl.end;

/**
 * @author xiewenwu
 */

import recommend.framework.Event;
import recommend.framework.annotation.Functor;
import recommend.framework.functor.AbstractFunctor;
import recommend.framework.log.impl.LogRequest;

@Functor
public class LogDump extends AbstractFunctor {
    @Override
    public int doInvoke(Event event) {
        System.out.println(this.getClass().getName());
        event.getLogManager().get(LogRequest.class);
        event.getLogManager().log();
        return 0;
    }
}
