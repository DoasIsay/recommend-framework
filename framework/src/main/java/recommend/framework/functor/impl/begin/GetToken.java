package recommend.framework.functor.impl.begin;

import recommend.framework.Event;
import recommend.framework.annotation.Functor;
import recommend.framework.functor.AbstractFunctor;

import java.util.UUID;

@Functor(name = "GetToken")
public class GetToken extends AbstractFunctor {
    @Override
    public Event doInvoke(Event event) {
        event.setId(UUID.randomUUID().toString());
        return event;
    }
}
