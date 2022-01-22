package recommend.framework.functor.impl.sort;

import recommend.framework.Item;
import recommend.framework.functor.AbstractSort;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class PredictSort extends AbstractSort {
    @Override
    public List<Item> sort(List<Item> items) {
        /*
        Map<String, List<AidItem>> chnAidItems = new HashMap<>();
        Map<String, Item> idItemMap = new HashMap<>();
        for (Item item: items) {
            chnAidItems.computeIfAbsent(item.getChn(), key->new ArrayList<>()).add(new AidItem(item.getId(), item.getChn(), item.getScore()));
            idItemMap.put(item.getId(), item);
        }

        List<Pair<String, List<AidItem>>> list = chnAidItems.entrySet().stream().map(entry->new Pair<String, List<AidItem>>(entry.getKey(), entry.getValue())).collect(Collectors.toList());
        return new PredictSorter().sortAll(list, null).stream().map(item->new Item(item.getAid(), idItemMap.get(item.getAid()).getChn(), item.getSortscore())).collect(Collectors.toList());
        */
        return null;
    }
}
