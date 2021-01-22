package cn.bracerframework.core.util;

import cn.hutool.db.sql.Condition;

/**
 * SQL相关工具类，包括相关SQL语句拼接等
 * 扩展自 {@link cn.hutool.db.sql.SqlUtil}
 *
 * @author Lifeng.Lin
 */
public class SqlUtil extends cn.hutool.db.sql.SqlUtil {

    /**
     * 模糊查询：% + 字段值 + %
     *
     * @param obj        变更对象
     * @param fieldNames 变更字段名
     */
    public static void addLike(Object obj, String fieldNames) {
        String[] fieldNameArray = fieldNames.split(",");
        for (String fieldName : fieldNameArray) {
            BeanUtil.setFieldValue(obj, fieldName, buildLikeValue(BeanUtil.getFieldStrValue(obj, fieldName), Condition.LikeType.Contains, false));
        }
    }

    /**
     * 模糊查询：% + 字段值
     *
     * @param obj        变更对象
     * @param fieldNames 变更字段名
     */
    public static void addLikeLeft(Object obj, String fieldNames) {
        String[] fieldNameArray = fieldNames.split(",");
        for (String fieldName : fieldNameArray) {
            BeanUtil.setFieldValue(obj, fieldName, buildLikeValue(BeanUtil.getFieldStrValue(obj, fieldName), Condition.LikeType.EndWith, false));
        }
    }

    /**
     * 模糊查询：字段值 + %
     *
     * @param obj        变更对象
     * @param fieldNames 变更字段名
     */
    public static void addLikeRight(Object obj, String fieldNames) {
        String[] fieldNameArr = fieldNames.split(",");
        for (String fieldName : fieldNameArr) {
            BeanUtil.setFieldValue(obj, fieldName, buildLikeValue(BeanUtil.getFieldStrValue(obj, fieldName), Condition.LikeType.StartWith, false));
        }
    }

}