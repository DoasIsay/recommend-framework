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
    public float weight = 1;

    @Override
    public void open(FunctorConfig config) {
        setType(Type.adjust);
        super.open(config);
    }

    public abstract void adjust(Item item);

    @Override
    public int doInvoke(Event event) {
        items.forEach(item -> {
            weight = 1;
            adjust(item);
            if (weight != 1) {
                item.addAdjust(getName(), weight);
            }
        });

        return 0;
    }
}
