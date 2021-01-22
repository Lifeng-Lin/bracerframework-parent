package cn.bracerframework.core.util;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.URLUtil;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

/**
 * 类工具类 <br>
 * 扩展自 {@link cn.hutool.core.util.ClassUtil}
 *
 * @author Lifeng.Lin
 */
public class ClassUtil extends cn.hutool.core.util.ClassUtil {

    /**
     * 获取类 Class
     *
     * @param className 类全名
     * @return 不存在返回 null
     */
    public static Class<?> getIfExist(String className) {
        try {
            return Class.forName(className);
        } catch (ClassNotFoundException e) {
            return null;
        }
    }

    /**
     * 判断是否为 Object 类型
     *
     * @param clazz 待判断类型
     * @return Object 类型返回 true
     */
    public static boolean isObject(Class<?> clazz) {
        return clazz != null && clazz.getSuperclass() == null;
    }

    /**
     * 判断是否为 Object 类型
     *
     * @param clazz 待判断类型
     * @return 不是 Object 类型返回 true
     */
    public static boolean isNotObject(Class<?> clazz) {
        return !isObject(clazz);
    }

    /**
     * 是否为静态字段
     *
     * @param field 字段
     * @return 是否为静态字段
     */
    public static boolean isStatic(Field field) {
        Assert.notNull(field, "Field to provided is null.");
        return Modifier.isStatic(field.getModifiers());
    }

    /**
     * 是否非静态字段
     *
     * @param field 字段
     * @return 是否非静态字段
     */
    public static boolean isNotStatic(Field field) {
        return !isStatic(field);
    }

    /**
     * 是否原始类型
     *
     * @param clazz 判断类型
     * @return 是否原始类型
     */
    public static boolean isPrimitive(Class<?> clazz) {
        return clazz != null && clazz.isPrimitive();
    }


    /**
     * 获取类路径下资源文件的路径
     *
     * @param clazz 类
     * @param path  获取的资源相对与类的路径，以'/'开头时，则是从项目的ClassPath根下获取资源
     * @return 资源文件的路径 中文字符未编译
     */
    public static String getResource(Class clazz, String path) {
        return getResource(clazz, path, true);
    }

    /**
     * 获取类路径下资源文件的路径
     *
     * @param clazz    类
     * @param path     获取的资源相对与类的路径，以'/'开头时，则是从项目的ClassPath根下获取资源
     * @param isDecode 中文字符是否URL反编译
     * @return 资源文件的路径
     */
    public static String getResource(Class clazz, String path, boolean isDecode) {
        path = clazz.getResource(path).getPath();
        if (isDecode) {
            path = URLUtil.decode(path, CharsetUtil.defaultCharset());
        }
        return path;
    }

}