package recommend.framework.util;

import org.apache.commons.lang.math.NumberUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.math3.util.Pair;

import java.util.*;
import java.util.function.Function;

public class StringHelper extends org.apache.commons.lang.StringUtils {
    public static Set<String> toSet(String str) {
        return toSet(str, ",", (s)->s);
    }

    public static Set<Integer> toIntSet(String str) { return toSet(str, ",",  s->NumberUtils.toInt(s,0));}

    public static <T> Set<T> toSet(String str, String separator, Function<String,T> function) {
        return Optional.ofNullable(str).map(s->Arrays.stream(s.split(separator)).filter(StringUtils::isNotEmpty).map(s1->function.apply(s1)).collect(java.util.stream.Collectors.toSet())).orElse(java.util.Collections.emptySet());
    }

    public static List<String> toList(String str) {
        return toList(str, ",", (s)->s);
    }

    public static <T> List<T> toList(String str, String separator, Function<String,T> function) {
        return Optional.ofNullable(str).map(s->Arrays.stream(s.split(separator)).filter(StringUtils::isNotEmpty).map(s1->function.apply(s1)).collect(java.util.stream.Collectors.toList())).orElse(java.util.Collections.emptyList());
    }

    public static Map<String, String> toMap(String str) {
        return toMap(str, ",", ":", k->(k), v->(v));
    }

    public static <K,V> Map<K, V> toMap(String str, String itemSeparator, String kvSeparator, Function<String,K> kFun, Function<String,V> vFun) {
        if (StringUtils.isEmpty(str)) return Collections.emptyMap();
        return Optional.ofNullable(str).map(s->Arrays.stream(s.split(itemSeparator)).filter(item->item.contains(kvSeparator)).map(kv->kv.split(kvSeparator)).map(fields->new Pair<>(kFun.apply(fields[0]),vFun.apply(fields[1]))).collect(java.util.stream.Collectors.toMap(Pair::getKey,Pair::getValue))).orElse(java.util.Collections.emptyMap());
    }
    public static void main(String[] argv) {
        System.out.println(toSet("1,2,3,4"));
        System.out.println(toMap("1:2,3:4"));
        System.out.println(toList("1,2,3,4"));
        System.out.println(toMap("1:2,3,4;3:4,5,6", ";", ":", k->(k), (v)->StringHelper.toList(v)));
    }
}
