package cn.bracerframework.core.util;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * 反射工具类
 * 扩展自 {@link cn.hutool.core.util.ReflectUtil}
 *
 * @author Lifeng.Lin
 */
public class ReflectUtil extends cn.hutool.core.util.ReflectUtil {

    /**
     * 获得一个类中所有带有指定注解的字段列表，包括其父类中的字段<br>
     * 如果子类与父类中存在同名字段，则这两个字段同时存在，子类字段在前，父类字段在后。
     *
     * @param beanClass       类
     * @param annotationClass 注解
     * @param comparator      排序方式
     * @return 字段列表
     */
    public static List<Field> getFields(Class<?> beanClass, Class<? extends Annotation> annotationClass, Comparator<? super Field> comparator) {
        List<Field> fields = getFields(beanClass, annotationClass);
        if (CollectionUtil.isNotEmpty(fields)) {
            fields.sort(comparator);
        }
        return fields;
    }

    /**
     * 获得一个类中所有带有指定注解的字段列表，包括其父类中的字段<br>
     * 如果子类与父类中存在同名字段，则这两个字段同时存在，子类字段在前，父类字段在后。
     *
     * @param beanClass       类
     * @param annotationClass 注解
     * @return 字段列表
     */
    public static List<Field> getFields(Class<?> beanClass, Class<? extends Annotation> annotationClass) {
        List<Field> fields = new ArrayList<>();
        Field[] fieldArr = getFields(beanClass);
        for (Field field : fieldArr) {
            if (AnnotationUtil.hasAnnotation(field, annotationClass)) {
                fields.add(field);
            }
        }
        return fields;
    }

}