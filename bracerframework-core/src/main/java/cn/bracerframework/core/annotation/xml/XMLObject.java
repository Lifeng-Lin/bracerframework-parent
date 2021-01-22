package cn.bracerframework.core.annotation.xml;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * XML 映射类注解 </br>
 * <pre>
 *     用于将 pojo 与 XML 文本做映射</br>
 * 运用场景：
 * 		1、XML 类报文解析成对象
 * </pre>
 *
 * @author Dracula
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface XMLObject {

    /**
     * XML 根节点名称</br>
     * 默认 root
     *
     * @return 根节点名称
     */
    String rootName() default "root";

}