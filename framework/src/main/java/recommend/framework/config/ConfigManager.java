package recommend.framework.config;

import com.ctrip.framework.apollo.ConfigFile;
import com.ctrip.framework.apollo.ConfigFileChangeListener;
import com.ctrip.framework.apollo.ConfigService;
import com.ctrip.framework.apollo.core.enums.ConfigFileFormat;
import com.ctrip.framework.apollo.model.ConfigFileChangeEvent;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import recommend.framework.util.JsonHelper;

import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

/**
 * @author xiewenwu
 * @date 2022/3/22 16:46
 */

@Slf4j
public class ConfigManager {
    static final boolean local = true;
    static ConcurrentHashMap<String, Config> configMap = new ConcurrentHashMap<>();

    @Getter
    static ConfigManager instance = new ConfigManager();

    private ConfigManager() {

    }

    static void load(String ns, ConfigFile configFile, Function<ConfigFile, Config> loader) {
        Config config = loader.apply(configFile);
        config.setValue("configFile", configFile);
        configMap.put(ns, config);
    }

    static public ConfigFile register(String ns, ConfigFileFormat configFileFormat, Function<ConfigFile, Config> loader) {
        ConfigFile configFile = null;

        if (!local) {
            configFile = ConfigService.getConfigFile(ns, configFileFormat);
            //注册回调更新时加载
            configFile.addChangeListener(new ConfigFileChangeListener() {
                @Override
                public void onChange(ConfigFileChangeEvent configChangeEvent) {
                    log.info("{} change, old: {}, new: {}", configChangeEvent.getNamespace()
                            , configChangeEvent.getOldValue()
                            , configChangeEvent.getNewValue());

                    load(ns, configFile, loader);
                }
            });
        }

        //注册时立即加载一次
        load(ns, configFile, loader);
        return configFile;
    }

    static public Config getConfig(String ns) {
        return configMap.get(ns);
    }

    static public Config toConfig(ConfigFile configFile) {
        ConfigFileFormat fileFormat = configFile.getConfigFileFormat();
        if (ConfigFileFormat.JSON.equals(fileFormat))
            return jsonToConfig(configFile);
        else if (ConfigFileFormat.YAML.equals(fileFormat))
            return yamlToConfig(configFile);
        else
            throw new RuntimeException("not support appConfig type " + fileFormat);
    }

    static Config jsonToConfig(ConfigFile configFile) {
        return new Config(JsonHelper.fromString(configFile.getContent()));
    }

    static Config yamlToConfig(ConfigFile configFile) {
        return new Config(JsonHelper.fromYamlString(configFile.getContent()));
    }
}
