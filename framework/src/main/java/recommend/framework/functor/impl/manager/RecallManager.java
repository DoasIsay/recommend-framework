package recommend.framework.functor.impl.manager;

/**
 * @author xiewenwu
 */

import recommend.framework.Event;
import recommend.framework.Item;
import recommend.framework.annotation.Functor;
import recommend.framework.config.FunctorConfig;
import recommend.framework.functor.AbstractRecall;
import recommend.framework.functor.AbstractManager;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

@Functor(name = "RecallManager")
public class RecallManager extends AbstractManager {

    @Override
    public int doInvokeParallel(Event event) {
        FilterManager filterManager = new FilterManager();
        List<recommend.framework.functor.Functor> functors = getFunctors();
        if (functors.isEmpty()) {
            return 0;
        }

        List<Callable<Integer>> recallTasks = new ArrayList<>(functors.size());
        functors.forEach(functor -> {
            ((AbstractRecall) functor).setFilterManager(filterManager);
            recallTasks.add(() -> functor.invoke(event));
        });

        int idx = 0;
        List<Item> result = new ArrayList<>();
        try {
            List<Future<Integer>> futures = threadPool.invokeAll(recallTasks, timeout, TimeUnit.MILLISECONDS);
            for (Future<Integer> future: futures) {
                if (future.isDone() && future.get() >= 0) {
                    result.addAll(functors.get(idx++).getResult());
                }
            }
            event.setItems(result);
            return 0;
        } catch (Exception e) {
            System.out.println("invoke "+ functors.get(idx).getClass());
            e.printStackTrace();
        }

        return -1;
    }
}

