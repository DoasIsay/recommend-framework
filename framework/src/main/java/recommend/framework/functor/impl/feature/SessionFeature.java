package recommend.framework.functor.impl.feature;

import recommend.framework.annotation.Functor;
import recommend.framework.functor.AbstractFeature;

@Functor(name = "SessionFeature")
public class SessionFeature extends AbstractFeature {
    @Override
    public Object get() {
        /*
        ReaderInfo readerInfo = ReaderClientPool.getInstance().sysncSendEx(userContext.getToken(),
                userContext.getHdid(), Long.parseLong(userContext.getUid()), userContext.getAccountUid(), userContext.isNewUser(),
                userContext.isStressTest(), userContext.getSwitchControl().getSessionServerClickNum());
        return readerInfo;
        */
        return new Object();
    }
}
