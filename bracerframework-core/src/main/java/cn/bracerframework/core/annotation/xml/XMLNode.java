package cn.bracerframework.core.annotation.xml;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * XML 叶子节点与基础类型对象的映射注解 </br>
 * <pre>
 *     用于将基础类型字段标注为 XML 的叶子节点</br>
 *     运用场景：
 *      1、将叶子节点映射到 pojo 指定的 int、date、string 类型字段
 * </pre>
 *
 * @author Dracula
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface XMLNode {

    /**
     * 对应的 XML 叶子节点名
     *
     * @return 节点名
     */
    String mapping();

    /**
     * 节点属性名
     * <pre>
     *     1、当该属性不为空时，会进行节点属性值的映射操作
     *     2、使用该属性，必须先配置对应的节点，并且该节点配置的字段必须在该节点属性的字段之前。例：
     *        @XMLNode(mapping = "Error", desc = "") private String error; @XMLNode(mapping = "Error", attrName = "code", desc = "") private String code; √
     *        @XMLNode(mapping = "Error", attrName = "code", desc = "") private String code; @XMLNode(mapping = "Error", desc = "") private String error; ×
     * </pre>
     *
     * @return 节点属性名
     */
    String attrName() default "";

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