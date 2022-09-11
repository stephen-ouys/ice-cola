package cn.stephen12.icecola.component.utils.collection;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * SetUtil (Hutool的 CollUtil是基于Collection进行交集、并集、差集运算，不能满足狭义上的集合运算)
 *
 * @author ouyangsheng
 * @since 2022-08-04
 **/
public class SetUtil {
    /**
     * 多个集合的并集<br>
     * 例如：集合1：[a, b, d, e, h]，集合2：[a, c, f]<br>
     * 结果：[a, b, c, d, e, f, h]
     *
     * @param <T>  集合元素类型
     * @param set1 集合1
     * @param set2 集合2
     * @return 并集的集合，返回 {@link Set}
     */
    public static <T> Set<T> union(Set<T> set1, Set<T> set2) {
        HashSet<T> result = new HashSet<>(set1);
        result.addAll(set2);
        return result;
    }

    /**
     * 两个集合的交集<br>
     * 例如：集合1：[a, b, c, d, e]，集合2：[a, b, c, f]<br>
     * 结果：[a, b, c]
     *
     * @param <T>  集合元素类型
     * @param set1 集合1
     * @param set2 集合2
     * @return 并集的集合，返回 {@link Set}
     */
    public static <T> Set<T> intersect(Set<T> set1, Set<T> set2) {
        return set1.stream().filter(set2::contains).collect(Collectors.toSet());
    }

    /**
     * 两个集合的差集<br>
     * 例如：集合1：[a, b, c, d, e]，集合2：[a, b, c, f]<br>
     * 结果：[d, e]
     *
     * @param set1
     * @param set2
     * @param <T>
     * @return
     */
    public static <T> Set<T> subtract(Set<T> set1, Set<T> set2) {
        HashSet<T> result = new HashSet<>(set1);
        result.removeAll(set2);
        return result;
    }

}
