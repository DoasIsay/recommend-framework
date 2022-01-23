package recommend.framework.config;

import com.alibaba.fastjson.JSON;
import com.ctrip.framework.apollo.model.ConfigFileChangeEvent;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import recommend.framework.util.ExtInfo;
import recommend.framework.util.GsonHelper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Getter
@Slf4j
public class Config extends ExtInfo {
    //private static ConfigFile configFile = ConfigService.getConfigFile("config", ConfigFileFormat.JSON);
    private Map<String, FunctorConfig> name2Config = new HashMap<>();
    List<FunctorConfig> functors;

    public Config() {
        //configFile.addChangeListener((event -> reload(event)));
        //reload(null);
    }

    public Config(Map<String, Object> map) {
        config.putAll(map);
    }

    public void reload(ConfigFileChangeEvent event) {
        //Config config = new Gson().fromJson(configFile.getContent(), Config.class);
        Config config = GsonHelper.get("framework/src/conf.json", Config.class);
        functors = config.getFunctors();
        name2Config = functors.stream().collect(Collectors.toMap(FunctorConfig::getName, v -> v));
        System.out.println(JSON.toJSONString(name2Config));
        //log.info("config {} change old {} new {}, now reload", event.getNamespace(), event.getOldValue(), event.getNewValue());
    }

    public static void main(String[] argv) {
        GsonHelper.get("framework/src/conf.json", Config.class);
    }
}
