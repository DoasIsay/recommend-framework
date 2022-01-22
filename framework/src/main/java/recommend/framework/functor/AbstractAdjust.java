package recommend.framework.functor;

import lombok.Data;
import recommend.framework.Event;
import recommend.framework.Item;
import recommend.framework.config.Config;

import java.util.stream.Collectors;

@Data
public abstract class AbstractAdjust extends AbstractFunctor {
    @Override
    public void open(Config config) {
        setType("adjust");
        super.open(config);
    }

    public abstract Item adjust(Item item);

    @Override
    public Event doInvoke(Event event) {
        event.setItems(getItems().stream().map(item -> {
            float score = item.getScore();
            Item tmp = adjust(item);

            if (score != tmp.getScore()) {
                //todo: 记录调权日志
            }
            return tmp;
        }).collect(Collectors.toList()));
        return event;
    }
}
