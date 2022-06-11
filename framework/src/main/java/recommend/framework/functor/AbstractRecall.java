package recommend.framework.functor;

/**
 * @author xiewenwu
 */

import lombok.Data;
import recommend.framework.Event;
import recommend.framework.Item;
import recommend.framework.config.FunctorConfig;
import recommend.framework.functor.impl.manager.FilterManager;

import java.util.List;

@Data
public abstract class AbstractRecall extends AbstractFunctor {
    @Override
    public void open(FunctorConfig config) {
        setType(Type.recall);
        super.open(config);
    }

    private FilterManager filterManager;

    public abstract List<Item> recall();

    @Override
    public int doInvoke(Event event) {
        List<Item> items = recall();
        items.forEach(item -> item.setChn(getName()));

        //统一过滤
        //filterManager.invoke(event);
        //召回截断
        int size = items.size();
        int exceptNum = expParam.getValue("exceptNum", 100);
        setResult(size > exceptNum? items.subList(0, exceptNum): items);

        if (size != 0) {
            //todo:记录召回日志
        }

        return size > 0? 0: -1;
    }
}
