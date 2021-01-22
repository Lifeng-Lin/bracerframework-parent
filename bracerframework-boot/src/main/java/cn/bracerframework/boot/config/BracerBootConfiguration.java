package cn.bracerframework.boot.config;

import cn.bracerframework.boot.filter.SecurityFilter;
import cn.bracerframework.boot.i.AuthorityVerification;
import cn.bracerframework.boot.properties.CorsProperties;
import cn.bracerframework.boot.properties.SecurityProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

/**
 * 定制 boot 应用环境配置
 *
 * @author Lifeng.Lin
 */
@EnableConfigurationProperties(value = {CorsProperties.class, SecurityProperties.class})
public class BracerBootConfiguration {

    /**
     * 安全访问配置
     *
     * @return
     */
    @Bean
    @ConditionalOnBean(AuthorityVerification.class)
    public SecurityFilter securityFilter() {
        return new SecurityFilter();
    }

    /**
     * 配置跨域
     *
     * @param corsProperties
     * @return 跨域处理过滤器
     */
    @Bean
    public FilterRegistrationBean corsFilter(CorsProperties corsProperties) {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsProperties);
        FilterRegistrationBean filter = new FilterRegistrationBean(new CorsFilter(source));
        filter.setOrder(0);
        return filter;
    }

}