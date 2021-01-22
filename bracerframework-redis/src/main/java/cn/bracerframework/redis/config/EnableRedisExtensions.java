package cn.bracerframework.redis.config;

import cn.bracerframework.redis.StringRedisService;
import org.springframework.context.annotation.Import;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 开启 Redis 扩展功能</br>
 * 当前仅实现类字符串的基础存储删除操作 {@link StringRedisService}
 *
 * @author Lifeng.Lin
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@Import(RedisExtensionsAutoConfiguration.class)
public @interface EnableRedisExtensions {
}