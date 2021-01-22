package cn.bracerframework.redis.config;

import cn.bracerframework.redis.StringRedisService;
import org.springframework.context.annotation.Bean;

/**
 * Redis 扩展功能配置
 *
 * @author Lifeng.Lin
 */
public class RedisExtensionsAutoConfiguration {

    @Bean
    public StringRedisService stringRedisService() {
        return new StringRedisService();
    }

}