package recommend.framework;

import lombok.Data;
import recommend.framework.util.ExtInfo;
import recommend.framework.util.StringHelper;

@Data
public class Item extends ExtInfo {
    public static Item EMPTY = new Item(null, 0f);
    //物料id
    String id;
    //召回通道
    String chn;
    //原始得分
    float score;
    //物料详细信息
    Object info;

    public Item(String id, float score) {
       this(id,null, score);
    }

    public Item(String id, String chn, float score) {
        this.id = id;
        this.chn = chn;
        this.score = score;
        this.info = getInfo();
    }
    public boolean isEmpty() {
        return StringHelper.isEmpty(id);
    }

    public Object getInfo() {
        //查询服务或本地cache自动获取
        return new Object();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Item) {
            Item item = (Item) obj;
            return item.getId().equals(getId());
        }
        return false;
    }
}
