package recommend.framework.functor.impl.manager;

import recommend.framework.Event;
import recommend.framework.Item;
import recommend.framework.annotation.Functor;
import recommend.framework.config.Config;
import recommend.framework.config.FunctorConfig;
import recommend.framework.functor.AbstractRecall;
import recommend.framework.functor.AbstractManager;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

@Functor(name = "recall")
public class RecallManager extends AbstractManager {
    public void open(FunctorConfig config) {
        setType("recall");
        setMode(Mode.parallel);
        super.open(config);
    }

   public Event doInvoke(Event event) {
        FilterManager filterManager = new FilterManager();
        List<Callable<Event>> recallTasks = new ArrayList<>();
        getFunctors().forEach(functor -> {
            ((AbstractRecall)functor).setFilterManager(filterManager);
            recallTasks.add(()->functor.invoke(event));
        });

        List<Item> result = new ArrayList<>();
        try {
            List<Future<Event>> recallFutures = threadPool.invokeAll(recallTasks, timeout, TimeUnit.MILLISECONDS);
            for (Future<Event> future: recallFutures) {
                Event tmp = future.get();
                result.addAll(tmp.getItems());
            }
            event.setItems(result);
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            event.setCode(-1);
        }
        return event;
    }
}

