package recommend.framework.config;

import com.ctrip.framework.apollo.ConfigFile;
import com.ctrip.framework.apollo.ConfigService;
import com.ctrip.framework.apollo.core.enums.ConfigFileFormat;
import com.ctrip.framework.apollo.model.ConfigFileChangeEvent;
import com.google.gson.Gson;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import recommend.framework.util.ExtInfo;

import java.util.Map;

@Data
@Slf4j
public class Config extends ExtInfo {
    private static ConfigFile configFile = ConfigService.getConfigFile("recommend/framework/config", ConfigFileFormat.JSON);

    public Config() {
        configFile.addChangeListener((event -> reload(event)));
    }

    public Config(Map<String, Object> map) {
        info.putAll(map);
    }

    public void reload(ConfigFileChangeEvent event) {
        info = new Gson().fromJson(configFile.getContent(), info.getClass());
        log.info("config {} change old {} new {}, now reload", event.getNamespace(), event.getOldValue(), event.getNewValue());
    }
}
