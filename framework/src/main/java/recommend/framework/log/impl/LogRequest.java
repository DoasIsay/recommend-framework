package recommend.framework.log.impl;

import recommend.framework.log.AbstractLog;

/**
 * @author xiewenwu
 * @date 2022/5/2 14:17
 */
public class LogRequest extends AbstractLog {
    @Override
    public Object format() {
        return event.getRequest();
    }
}
