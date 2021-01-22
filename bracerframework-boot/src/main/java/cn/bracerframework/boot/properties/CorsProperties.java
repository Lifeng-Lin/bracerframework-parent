package cn.bracerframework.boot.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.web.cors.CorsConfiguration;

/**
 * CORS 跨域属性
 *
 * @author Lifeng.Lin
 */
@ConfigurationProperties(prefix = CorsProperties.PREFIX)
public class CorsProperties extends CorsConfiguration {
    public static final String PREFIX = "bracerframework.cors";

}