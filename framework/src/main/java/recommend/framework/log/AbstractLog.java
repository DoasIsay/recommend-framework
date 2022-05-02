package recommend.framework.log;

import com.alibaba.fastjson.JSON;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.log4j.Logger;
import recommend.framework.Context;
import recommend.framework.Event;
import recommend.framework.util.KafkaHelper;

/**
 * @author xiewenwu
 * @date 2022/5/2 12:55
 */
@Slf4j
public abstract class AbstractLog implements Log, Runnable {
    static final Logger logDumper = Logger.getLogger("logDump");
    static KafkaProducer<String, String> kafkaProducer = KafkaHelper.get("logDump");
    static final String SPLIT = "${sp}";
    String resId;

    public Event event;

    @Getter
    boolean async = true;
    String type = "disk";

    public AbstractLog() {
        this.resId = this.getClass().getSimpleName();
    }

    public void init(Event event) {
        this.event = event;
    }

    private final String getLogStr() {
        Object obj = format();
        String json = JSON.toJSONString(obj);
        StringBuilder sb = new StringBuilder();
        sb.append(System.currentTimeMillis())
                .append(SPLIT)
                .append(event.getId())
                .append(SPLIT)
                .append("ip")
                .append(SPLIT)
                .append(event.getContext(Context.class).getUid())
                .append(SPLIT)
                .append(resId)
                .append(SPLIT)
                .append(json);
        return sb.toString();
    }

    @Override
    public final void log() {
        String logStr = getLogStr();
        if ("disk".equals(type)) {
            logDumper.info(logStr);
        } else if ("kafka".equals(type)) {
            kafkaProducer.send(new ProducerRecord("logDump", logStr));
        }
    }


    @Override
    public final void run() {
        try {
            log();
        } catch (Exception e) {
            log.error("{} format error: ", resId);
        }
    }
}
