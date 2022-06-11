package recommend.framework.util;

import com.google.protobuf.Descriptors;
import com.google.protobuf.Message;

import java.util.HashMap;
import java.util.Map;
/**
 * @author xiewenwu
 */
public class PbHelper {
    public static Map<String, Object> toMap(Message message) {
        Map<String, Object> map = new HashMap<>(message.getAllFields().size());
        for (Map.Entry<Descriptors.FieldDescriptor, Object> entry : message.getAllFields().entrySet()) {
            map.put(entry.getKey().getName(), entry.getValue());
        }
        return map;
    }
}
