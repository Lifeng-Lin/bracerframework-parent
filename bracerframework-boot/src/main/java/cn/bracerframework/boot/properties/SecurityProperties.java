package cn.bracerframework.boot.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.HashSet;
import java.util.Set;

/**
 * 安全校验过滤器配置项
 *
 * @author Lifeng.Lin
 */
@ConfigurationProperties(prefix = SecurityProperties.PREFIX)
public class SecurityProperties {
    public static final String PREFIX = "bracerframework.security";

    /**
     * 公用访问路径
     */
    private Set<String> publicPaths = new HashSet<>();

    /**
     * @return 公用访问路径
     */
    public Set<String> getPublicPaths() {
        return publicPaths;
    }

    /**
     * @param publicPaths 公用访问路径
     */
    public void setPublicPaths(Set<String> publicPaths) {
        this.publicPaths = publicPaths;
    }

}