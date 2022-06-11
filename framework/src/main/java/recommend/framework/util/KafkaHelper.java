package recommend.framework.util;

import com.ctrip.framework.apollo.ConfigFile;
import com.ctrip.framework.apollo.core.enums.ConfigFileFormat;
import org.apache.kafka.clients.producer.KafkaProducer;
import recommend.framework.config.Config;
import recommend.framework.config.ConfigManager;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author xiewenwu
 */
public class KafkaHelper {
    static {
        ConfigManager.register("kafka", ConfigFileFormat.JSON, KafkaHelper::load);
    }

    static ConcurrentHashMap<String, KafkaProducer> kafkaMap;

    static Config load(ConfigFile configFile) {
        if (kafkaMap == null) {
            kafkaMap = new ConcurrentHashMap<>();
        }
        Config config = ConfigManager.toConfig(configFile);
        config.getConfig().forEach((k, v) -> {
            kafkaMap.put(k, new KafkaProducer((Map<String,Object>)v));
        });

        return config;
    }

    public static KafkaProducer get(String name) {
        if (kafkaMap != null) {
            return kafkaMap.get(name);
        }

        return null;
    }
}
