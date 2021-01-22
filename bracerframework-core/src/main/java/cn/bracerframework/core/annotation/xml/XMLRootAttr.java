package cn.bracerframework.core.annotation.xml;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * XML 根节点属性注解 </br>
 * <pre>
 *     1、用于将基础类型字段标注为 XML 的根节点属性
 *     2、仅在被 {@link XMLObject} 标注的映射类中可用
 * </pre>
 *
 * @author Lifeng.Lin
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface XMLRootAttr {

    /**
     * 节点属性名
     *
     * @return 节点属性名
     */
    String attrName();

    /**
     * 描述
     *
     * @return 描述
     */
    String desc();

    /**
     * 值长度验证</br>
     * 默认 0 不进行验证
     *
     * @return 值允许最大长度
     */
    int length() default 0;

    /**
     * 节点是否必输</br>
     * 默认 false
     *
     * @return 是否必输
     */
    boolean required() default false;

    /**
     * 节点为时间类型是的格式</br>
     * 默认：yyyy-MM-dd HH:mm:ss
     *
     * @return 时间格式
     */
    String format() default "yyyy-MM-dd HH:mm:ss";

}