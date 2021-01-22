package cn.bracerframework.web.log.config;

import cn.bracerframework.web.log.LogAspect;
import org.springframework.context.annotation.Bean;

/**
 * 日志切面配置类
 *
 * @author Lifeng.Lin
 */
public class LogAspectAutoConfiguration {

    @Bean
    public LogAspect logAspect() {
        return new LogAspect();
    }

}