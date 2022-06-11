package recommend.framework.functor.impl.begin;

/**
 * @author xiewenwu
 */

import recommend.framework.Event;
import recommend.framework.annotation.Functor;
import recommend.framework.config.Config;
import recommend.framework.functor.AbstractFunctor;

import java.util.Collections;

@Functor(name = "ABExpParam")
public class ExpParamPrepare extends AbstractFunctor {
    public String tab;
    @Override
    public void init() {
        super.init();
        //tab = expParam.getString("tab", "");
    }

    @Override
    public int doInvoke(Event event) {
        /*
        ExpReq req = new ExpReq(userContext.getHdid(), userContext.getUid(), userContext.getAccountUid(),
                tab, userContext.isNewUser(), userContext.getToken());
        userContext.setOlExpCtx(ExpCore.getInstance().getAllLayerExps(req));
        */

        context.setExpConfig(new Config(Collections.emptyMap()));
        return 0;
    }
}
