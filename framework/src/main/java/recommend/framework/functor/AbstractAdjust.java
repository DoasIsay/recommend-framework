package recommend.framework.functor;

/**
 * @author xiewenwu
 */

import lombok.Data;
import recommend.framework.Event;
import recommend.framework.Item;
import recommend.framework.config.FunctorConfig;

import java.util.stream.Collectors;

@Data
public abstract class AbstractAdjust extends AbstractFunctor {
    @Override
    public void open(FunctorConfig config) {
        setType(Type.adjust);
        super.open(config);
    }

    public abstract Item adjust(Item item);

    @Override
    public int doInvoke(Event event) {
        event.setItems(getItems().stream().map(item -> {
            float score = item.getScore();
            Item tmp = adjust(item);

            if (score != tmp.getScore()) {
                //todo: 记录调权日志
            }
            return tmp;
        }).collect(Collectors.toList()));
        return 0;
    }
}
