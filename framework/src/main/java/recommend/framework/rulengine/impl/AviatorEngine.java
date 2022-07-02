package recommend.framework.rulengine.impl;

import com.googlecode.aviator.AviatorEvaluator;
import com.googlecode.aviator.Options;
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
    static {
        AviatorEvaluator.setOption(Options.NIL_WHEN_PROPERTY_NOT_FOUND, true);
    }

    public static AviatorEngine getInstance() {return instance;}

    @Override
    public <T> T execute(String express, Object map) {
        return (T) AviatorEvaluator.compile(express, true).execute((Map<String,Object>) map);
    }

    @Data
    static class Test {
        public int a = 100;
    }
    public static void main(String[] argv) {
        Map map = new HashMap<String, Object>(){{
            put("a", true);
            put("b", false);
            put("offset", 0);
            put("test", new Test());
            put("map", new HashMap<String,Object>(){{
                put("b", 123);
            }});
        }};
        String express = "c!=nil && c || false";
        System.out.println(java.util.Optional.ofNullable(AviatorEngine.getInstance().execute(express, map)));
    }
}
