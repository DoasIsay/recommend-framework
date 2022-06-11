package recommend.framework;

import lombok.Data;
import recommend.framework.config.Config;

/**
 * @author xiewenwu
 */
@Data
public class Context {
    int offset = 0;
    int size = 10;
    String city="";
    String version="";
    String uid="";
    String os ="ios";
    //实验参数
    Config expConfig;
}
