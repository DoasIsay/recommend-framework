package recommend.framework.functor;

/**
 * @author xiewenwu
 */

import lombok.Data;
import recommend.framework.Event;
import recommend.framework.Item;
import recommend.framework.config.FunctorConfig;

@Data
public abstract class AbstractAdjust extends AbstractFunctor {
    @Override
    public void open(FunctorConfig config) {
        setType(Type.adjust);
        super.open(config);
    }

    public abstract float adjust(Item item);

    @Override
    public int doInvoke(Event event) {
        items.forEach(item -> {
            float oldQ = item.getAdjQ();
            float weight = adjust(item);
            float newQ = item.getAdjQ();

            if (oldQ != newQ) {
                item.addAdjust(getName(), weight);
            }
        });

        return 0;
    }
}
