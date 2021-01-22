package cn.bracerframework.web.permissions.config;

import cn.bracerframework.web.permissions.PermissionsAspect;
import org.springframework.context.annotation.Bean;

/**
 * 服务调用权限控制配置类
 *
 * @author Lifeng.Lin
 */
public class ServerPermissionsAutoConfiguration {

    @Bean
    public PermissionsAspect permissionsAspect() {
        return new PermissionsAspect();
    }

}