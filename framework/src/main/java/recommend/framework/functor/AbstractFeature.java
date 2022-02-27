package recommend.framework.functor;

import recommend.framework.Event;
import recommend.framework.config.FunctorConfig;

public abstract class AbstractFeature extends AbstractFunctor{
    @Override
    public void open(FunctorConfig config) {
        setType("feature");
        super.open(config);
    }
    public abstract Object get();

    @Override
    public Event doInvoke(Event event) {
        event.setValue(this.getClass().getSimpleName(), get());
        //todo:记录实时特征日志？
        return event;
    }
}
