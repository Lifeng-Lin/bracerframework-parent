package cn.bracerframework.web.permissions.annotation;

import cn.hutool.extra.spring.SpringUtil;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 服务调用权限控制</br>
 * <pre>
 * 不支持在 private 方法上使用</br>
 * Spring AOP 不支持拦截方法调用其他内部方法，因此在被内部方法调用时，该注解无效。解决方法：</br>
 *  1、通过不同类之间自动注入调用</br>
 *  2、通过 {@link SpringUtil} 的方法 getBean 获取对象进行调用</br>
 *  3、通过 ((ClassName)AopContext.currentProxy()).method 调用方法
 * </pre>
 *
 * @author Lifeng.Lin
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface RequiresPermissions {

    /**
     * 需要的权限标识
     *
     * @return 需要的权限标识
     */
    String[] value();

}