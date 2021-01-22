package cn.bracerframework.core.util;

import cn.hutool.core.convert.Convert;

/**
 * Bean工具类
 * 扩展自 {@link cn.hutool.core.bean.BeanUtil}
 *
 * @author Dracula
 */
public class BeanUtil extends cn.hutool.core.bean.BeanUtil {

    /**
     * 获得字段字符串值，通过反射直接获得字段值，并不调用getXXX方法<br>
     * 对象同样支持Map类型，fieldNameOrIndex即为key
     *
     * @param bean             Bean对象
     * @param fieldNameOrIndex 字段名或序号，序号支持负数
     * @return 字段字符串值
     */
    public static String getFieldStrValue(Object bean, String fieldNameOrIndex) {
        return Convert.toStr(getFieldValue(bean, fieldNameOrIndex));
    }

    /**
     * 对象属性拷贝
     *
     * @param source    源对象
     * @param target    目标对象
     * @param fieldName 拷贝字段名
     */
    public static void copyProperties(Object source, Object target, String fieldName) {
        setFieldValue(target, fieldName, getFieldValue(source, fieldName));
    }

    /**
     * 对象属性拷贝
     *
     * @param source          源对象
     * @param sourceFieldName 源字段名
     * @param target          目标对象
     * @param targetFieldName 目标字段名
     */
    public static void copyProperties(Object source, String sourceFieldName, Object target, String targetFieldName) {
        setFieldValue(target, targetFieldName, getFieldValue(source, sourceFieldName));
    }

}