package recommend.framework;

import lombok.AllArgsConstructor;
import lombok.Data;
import recommend.framework.util.ExtInfo;
import recommend.framework.util.StringHelper;

import java.util.ArrayList;
import java.util.List;

@Data
public class Item extends ExtInfo {
    //物料id
    String id;
    //召回通道
    String chn;
    //分数
    Score score;
    //物料详细信息
    transient Object info;

    public static Item EMPTY = new Item(null, 0f);

    public Item(String id, float score) {
       this(id,null, score);
    }

    public Item(String id, String chn, float score) {
        this.id = id;
        this.chn = chn;
        this.score = new Score(score);
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

    @Data
    public static class Score {
        public Score(float original) {
            this.origQ = original;
        }
        //原始得分
        float origQ;
        //排序得分
        float sortQ;
        //调权得分
        float adjQ;
        //调权信息
        List<Adjust> adjusts;
        public void addAdjust(Adjust adjust) {
            if (adjusts == null) {
                adjusts = new ArrayList<>();
            }
            adjusts.add(adjust);
        }
    }

    @Data
    @AllArgsConstructor
    public static class Adjust {
        String name;
        float weight;
    }

    public void setOrigQ(float q) {
        score.setOrigQ(q);
    }

    public void setSortQ(float q) {
        score.setSortQ(q);
    }

    public void setAdjQ(float q) {
        score.setAdjQ(q);
    }

    public float getOrigQ() {
        return score.getOrigQ();
    }

    public float getSortQ() {
        return score.getSortQ();
    }

    public float getAdjQ() {
        return score.getAdjQ();
    }

    public void addAdjust(String name, float weight) {
        score.addAdjust(new Adjust(name, weight));
    }
}
