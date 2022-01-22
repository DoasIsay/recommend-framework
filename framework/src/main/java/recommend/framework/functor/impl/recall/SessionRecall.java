package recommend.framework.functor.impl.recall;

import recommend.framework.Item;
import recommend.framework.annotation.Functor;
import recommend.framework.functor.AbstractRecall;
import recommend.framework.rulengine.RuleEngine;
import recommend.framework.rulengine.RuleEngineFactory;

import java.util.Collections;
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
        /*
        ReaderInfo readerInfo= event.getUserFeature("session", null);
        return readerInfo.getAsFeasByMinute(5).stream()
                .filter(feature->(boolean) ruleEngine.execute(express, PbHelper.toMap((Message) feature)))
                .map(fea->new Item(String.valueOf(fea.getAid()), 0f))
                .collect(Collectors.toList());

         */
        return Collections.emptyList();
    }
}
