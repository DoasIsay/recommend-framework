package recommend.framework.functor;

import recommend.framework.Event;

public abstract class AbstractFeature extends AbstractFunctor{
    public abstract Object get();

    @Override
    public Event doInvoke(Event event) {
        event.setUserFeature(getName(), get());
        //todo:记录实时特征日志？
        return event;
    }
}
