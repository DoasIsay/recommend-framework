package recommend.framework.functor.impl.begin;

import recommend.framework.Event;
import recommend.framework.annotation.Functor;
import recommend.framework.functor.AbstractFunctor;

@Functor(name = "ABExpParam")
public class ExpParamPrepare extends AbstractFunctor {
    public String tab;
    @Override
    public void init() {
        super.init();
        tab = expParam.getString("tab", "");
    }

    @Override
    public Event doInvoke(Event event) {
        /*
        ExpReq req = new ExpReq(userContext.getHdid(), userContext.getUid(), userContext.getAccountUid(),
                tab, userContext.isNewUser(), userContext.getToken());
        userContext.setOlExpCtx(ExpCore.getInstance().getAllLayerExps(req));
        */

        return event;
    }
}
