package recommend.framework.config;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.ctrip.framework.apollo.ConfigFile;
import com.ctrip.framework.apollo.core.enums.ConfigFileFormat;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import recommend.framework.util.JsonHelper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Data
@Slf4j
public class AppConfig extends Config {
    private volatile Map<String, FunctorConfig> name2Config = new HashMap<>();
    private volatile List<FunctorConfig> functors;

    public AppConfig() {
        ConfigManager.register("appConfig", ConfigFileFormat.JSON, this::load);
    }

    public Config load(ConfigFile configFile) {
        AppConfig appConfig;
        if (configFile != null) {
            appConfig = JsonHelper.fromString(configFile.getContent(), AppConfig.class);
        } else {
            appConfig = JsonHelper.fromFile("framework/src/conf.json", AppConfig.class);
        }

        functors = appConfig.getFunctors();
        name2Config = functors.stream().filter(functorConfig -> StringUtils.isNotEmpty(functorConfig.getName())).collect(Collectors.toMap(FunctorConfig::getName, v -> v));
        System.out.println(JSON.toJSONString(name2Config, SerializerFeature.PrettyFormat));
        log.info(JSON.toJSONString(name2Config, SerializerFeature.PrettyFormat));
        return appConfig;
    }

    public static void main(String[] argv) {
        JsonHelper.fromFile("framework/src/conf.json", AppConfig.class);
    }
}
