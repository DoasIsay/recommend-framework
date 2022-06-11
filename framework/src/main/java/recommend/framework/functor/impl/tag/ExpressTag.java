package recommend.framework.functor.impl.tag;

import org.apache.commons.lang.StringUtils;
import recommend.framework.annotation.Functor;
import recommend.framework.functor.AbstractTag;
import recommend.framework.rulengine.RuleEngineFactory;

/**
 * @author xiewenwu
 * 使用规则引擎通过已知用户特征对用户打标签
 * 简单tag可直接写在算子的express
 * 复杂tag可先使用ExpressTag打好标签以免重复计算
 */

@Functor
public class ExpressTag extends AbstractTag {
    @Override
    public String get() {
        String express = expParam.getString("tagExpress","");
        if (StringUtils.isEmpty(express)) {
            return null;
        }

        String tag = (String) RuleEngineFactory.get(expParam.getString("ruleEngine", "aviator")).execute(express, userFeatures);
        System.out.println(tag);
        return tag;
    }
}
