package recommend.framework.functor;

/**
 * @author xiewenwu
 */

import recommend.framework.Event;
import recommend.framework.config.FunctorConfig;

public abstract class AbstractFeature extends AbstractFunctor{
    @Override
    public void open(FunctorConfig config) {
        setType(Type.feature);
        super.open(config);
    }
    public abstract Object get();

    @Override
    public int doInvoke(Event event) {
        event.setUserFeature(this.getClass().getSimpleName(), get());
        //todo:记录实时特征日志？
        return 0;
    }
}
