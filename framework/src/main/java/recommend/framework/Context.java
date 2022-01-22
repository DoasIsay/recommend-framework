package recommend.framework;

import lombok.Data;

import java.util.Map;

@Data
public class Context {
    Map<String, Object> expMap;
    int offset;
    int expectNum;
    String os;
    String city;
    int actDay;
    String version;
    String hdid;
}
