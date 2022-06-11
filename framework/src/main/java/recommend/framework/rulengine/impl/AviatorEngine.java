package recommend.framework.rulengine.impl;

import com.googlecode.aviator.AviatorEvaluator;
import lombok.Data;
import recommend.framework.rulengine.RuleEngine;

import java.util.HashMap;
import java.util.Map;

/**
 * @author xiewenwu
 */
@Data
public class AviatorEngine implements RuleEngine {
    private AviatorEngine() {}
    private static AviatorEngine instance = new AviatorEngine();
    public static AviatorEngine getInstance() {return instance;}

    @Override
    public Object execute(String express, Object map) {
        return AviatorEvaluator.compile(express, true).execute((Map<String,Object>) map);
    }

    public static void main(String[] argv) {
        Map map = new HashMap<String, Object>(){{
            put("a", "1");
            put("offset", 0);
        }};
        System.out.println(AviatorEngine.getInstance().execute("'2'==b", map));
    }
}
