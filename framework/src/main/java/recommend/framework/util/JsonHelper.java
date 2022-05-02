package recommend.framework.util;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.representer.Representer;

import java.io.*;
import java.util.Optional;

@Slf4j
public class JsonHelper {
    static public <T> T fromFile(String path, Class<T> c) {
        return Optional.ofNullable(fromFile(path)).map(jsonObject -> jsonObject.toJavaObject(c)).orElse(null);
    }

    static public JSONObject fromString(String str) {
        return JSONObject.parseObject(str);
    }

    static public <T> T fromString(String str, Class<T> c) {
        try {
            return (T) JSONObject.parseObject(str, c);
        } catch (Exception e) {
            log.error("json parse {} error: {}", str, e);
            System.out.println(e);
        }

        return null;
    }

    static public JSONObject fromYamlString(String str) {
        Representer representer = new Representer();
        representer.getPropertyUtils().setSkipMissingProperties(true);

        return JSONObject.parseObject(JSONObject.toJSONString(new Yaml(representer).load(str)));
    }

    static public JSONObject fromYamlFile(String path) {
        Representer representer = new Representer();
        representer.getPropertyUtils().setSkipMissingProperties(true);

        try {
            return JSONObject.parseObject(JSONObject.toJSONString(new Yaml(representer).load(new FileInputStream(path))));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return null;
    }

    static public JSONObject fromFile(String path) {
        StringBuilder sb = new StringBuilder();
        try {
            BufferedReader br = new BufferedReader(new FileReader(new File(path)));
            while (true) {
                String line = br.readLine();
                if (line != null) {
                    sb.append(line);
                } else {
                    break;
                }
            }

            return JSONObject.parseObject(sb.toString());
        } catch (Exception e) {
            log.error("json parse {} error: {}", sb.toString(), e);
            e.printStackTrace();
        }

        return null;
    }
}
