package recommend.framework.rulengine;

import recommend.framework.rulengine.impl.AviatorEngine;

public class RuleEngineFactory {
    public static RuleEngine get(String type) {
        switch (type) {
            case "aviator": return AviatorEngine.getInstance();
            default: throw new RuntimeException("not support RuleEngine type: " + type);
        }
    }
}
