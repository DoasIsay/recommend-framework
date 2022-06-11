package recommend.framework.log.impl;

import recommend.framework.log.AbstractLog;

/**
 * @author xiewenwu
 */
public class LogRequest extends AbstractLog {
    @Override
    public Object format() {
        return event.getRequest();
    }
}
