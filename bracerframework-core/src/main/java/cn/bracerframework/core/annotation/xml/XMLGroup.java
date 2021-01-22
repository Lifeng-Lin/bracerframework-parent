package cn.bracerframework.core.annotation.xml;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.HashMap;

/**
 * XML 复合节点与自定义对象的映射注解 </br>
 * <pre>
 *     用于将  {@link java.util.List}、{@link java.util.Map} 或者任意自定义 pojo 类型字段标注为 XML 的复合节点</br>
 *     当节点映射对象类型非 {@link java.util.List}，此时如果该节点搜寻到结果为列表，则取匹配到的第一个节点做映射</br>
 *     运用场景：
 *      1、将 XML 的复合节点映射为 pojo 对象、{@link java.util.Map} 或者 {@link java.util.List} 列表</br>
 *      2、如果复合节点为列表，节点映射字段应为 {@link java.util.List} 类型，并配置注解的 type 属性
 * </pre>
 *
 * @author Lifeng.Lin
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface XMLGroup {

    /**
     * 对应的 XML 复合节点名
     *
     * @return 节点名
     */
    String mapping();

    /**
     * 描述
     *
     * @return 描述
     */
    String desc();

    /**
     * 当字段类型为 {@link java.util.List} 时设置该对象，指定 {@link java.util.List} 内存放的对象类型</br>
     * 默认 {@link java.util.HashMap}
     *
     * @return 对象类型
     */
    Class<?> type() default HashMap.class;

}