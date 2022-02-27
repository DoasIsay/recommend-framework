package recommend.framework.functor;

import recommend.framework.Event;
import recommend.framework.config.FunctorConfig;

public interface Functor {
    void open(FunctorConfig config);

    void init();

    Event invoke(Event event);

    Event doInvoke(Event event);

    void close();

    void setName(String name);

    String getName();
}
