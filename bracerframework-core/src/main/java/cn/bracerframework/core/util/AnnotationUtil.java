package cn.bracerframework.core.util;

import cn.bracerframework.core.exception.SystemException;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.ReflectUtil;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 * 注解工具类<br>
 * 扩展自 {@link cn.hutool.core.annotation.AnnotationUtil}
 *
 * @author Lifeng.Lin
 */
public class AnnotationUtil extends cn.hutool.core.annotation.AnnotationUtil {

    /**
     * 获取指定注解属性的值<br>
     * 如果无指定的属性方法返回null
     *
     * @param beanClass      被查找字段的类,不能为null
     * @param fieldName      字段名
     * @param annotationType 注解类型
     * @param propertyName   属性名，例如注解中定义了name()方法，则 此处传入name
     * @param <T>            注解值类型
     * @return 注解对象指定的属性值
     */
    public static <T> T getFieldAnnotationValue(Class<?> beanClass, String fieldName, Class<? extends Annotation> annotationType, String propertyName) {
        Field field = ReflectUtil.getField(beanClass, fieldName);
        if (field == null) {
            throw new SystemException(beanClass.getSimpleName() + "." + fieldName + "未配置注解" + annotationType.getSimpleName());
        }
        return getAnnotationValue(field, annotationType, propertyName);
    }

    /**
     * 获取所有带有指定注解的字段
     *
     * @param beanClass      被查找字段的类,不能为null
     * @param annotationType 注解类型
     * @return 所有带有指定注解的字段
     */
    public static Map<String, Field> getAllAnnotationField(Class<?> beanClass, Class<? extends Annotation> annotationType) {
        Field[] fields = ReflectUtil.getFields(beanClass);
        if (ArrayUtil.isNotEmpty(fields)) {
            Map<String, Field> temp = new HashMap<>(fields.length);
            for (Field field : fields) {
                if (getAnnotation(field, annotationType) != null) {
                    temp.put(field.getName(), field);
                }
            }
            return temp;
        }
        return new HashMap<>(0);
    }

}