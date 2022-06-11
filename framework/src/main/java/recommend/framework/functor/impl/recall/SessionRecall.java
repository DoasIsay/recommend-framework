package recommend.framework.functor.impl.recall;

/**
 * @author xiewenwu
 */

import recommend.framework.Item;
import recommend.framework.annotation.Functor;
import recommend.framework.functor.AbstractRecall;
import recommend.framework.rulengine.RuleEngine;
import recommend.framework.rulengine.RuleEngineFactory;

import java.util.ArrayList;
import java.util.List;

@Functor(name = "SessionRecall")
public class SessionRecall extends AbstractRecall {
    String express;
    RuleEngine ruleEngine;

    @Override
    public void init() {
        super.init();
        //种子挑选规则
        express = expParam.getString("seedExpress", "duration>10 && views>1 || likes>0");
        //使用那种规则引擎
        ruleEngine = RuleEngineFactory.get(expParam.getString("ruleEngine", "aviator"));
    }
    @Override
    public List<Item> recall() {
        return new ArrayList<Item>(){{
            add(new Item("1234", 0f));
            add(new Item("1235", 0f));
            add(new Item("1236", 0f));
            add(new Item("1237", 0f));
        }};
    }
}
