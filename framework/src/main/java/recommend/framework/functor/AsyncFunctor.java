package recommend.framework.functor;

import recommend.framework.Event;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.TimeUnit;

public abstract class AsyncFunctor<T> extends AbstractFunctor{
    private ArrayBlockingQueue<T> queue;

    @Override
    public void init() {
        super.init();
        queue = new ArrayBlockingQueue<T>(config.getValue("queueSize", 1));
    }

    public boolean send(T obj, boolean sync) {
        if (!sync) return queue.offer(obj);
        try {
            queue.put(obj);
            return true;
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return false;
    }

    public abstract void process(T obj);

    @Override
    public Event invoke(Event event) {
        try {
            while (true) {
                process(queue.poll(100, TimeUnit.MILLISECONDS));
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }
}
