package recommend.framework.functor.impl.filter;

import recommend.framework.Item;
import recommend.framework.annotation.Functor;
import recommend.framework.functor.AbstractFilter;
import recommend.framework.util.StringHelper;

import java.util.Optional;
import java.util.Set;

@Functor(name = "BizFilter")
public class BizFilter extends AbstractFilter {
    private Set<String> bizSet;

    @Override
    public void init() {
        super.init();
        bizSet = StringHelper.toSet(config.getValue("biz", ""));
    }

    @Override
    public boolean filter(Item item) {
        return Optional.ofNullable(item.getInfo())
                .map(anchor -> bizSet.contains(anchor))
                .orElse(false);
    }
}
