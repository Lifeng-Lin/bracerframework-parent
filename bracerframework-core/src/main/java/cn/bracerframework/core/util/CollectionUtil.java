package cn.bracerframework.core.util;

import java.util.Collection;

/**
 * 集合相关工具类，包括数组
 * 扩展自 {@link cn.hutool.core.collection.CollectionUtil}
 *
 * @author Dracula
 */
public class CollectionUtil extends cn.hutool.core.collection.CollectionUtil {

    /**
     * 判断两个集合长度是否相等
     *
     * @param c1 待判断集合
     * @param c2 待判断集合
     * @return 是否相等
     */
    public static boolean isEqLength(Collection<?> c1, Collection<?> c2) {
        return c1 != null && c2 != null && c1.size() == c2.size();
    }

}