package cn.bracerframework.mybatis.plus.config;

import org.springframework.context.annotation.Import;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 启动 Mybatis Plus 扩展功能
 *
 * @author Lifeng.Lin
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@Import(EnableMybatisPlusAutoConfiguration.class)
public @interface EnableMybatisPlusExtensions {
}