package cn.bracerframework.boot.config;

import cn.bracerframework.web.mvc.config.EnableBracerMVC;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 启用定制 boot 应用
 *
 * @author Lifeng.Lin
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@SpringBootApplication
@EnableBracerMVC
@Import(BracerBootConfiguration.class)
public @interface BracerBootApplication {
}