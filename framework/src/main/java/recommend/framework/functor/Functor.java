package recommend.framework.functor;

import recommend.framework.Event;
import recommend.framework.config.FunctorConfig;

public interface Functor {
    void open(FunctorConfig config);

    void init();

    int invoke(Event event);

    int doInvoke(Event event);

    void close();

    void setName(String name);

    String getName();

    <T> T getResult();
}
