package recommend.framework.rulengine.impl;

import com.googlecode.aviator.AviatorEvaluator;
import lombok.Data;
import recommend.framework.rulengine.RuleEngine;

import java.util.Map;

@Data
public class AviatorEngine implements RuleEngine {
    private AviatorEngine() {}
    private static AviatorEngine instance = new AviatorEngine();
    public static AviatorEngine getInstance() {return instance;}

    @Override
    public Object execute(String express, Object map) {
        return AviatorEvaluator.compile(express, true).execute((Map<String,Object>)map);
    }
}
