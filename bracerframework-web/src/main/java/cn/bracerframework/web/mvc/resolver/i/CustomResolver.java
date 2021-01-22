package cn.bracerframework.web.mvc.resolver.i;

import org.springframework.web.method.support.HandlerMethodArgumentResolver;

import java.util.List;

/**
 * 自定义参数解析器
 *
 * @author Lifeng.Lin
 */
public interface CustomResolver {

    /**
     * 添加自定义参数解析器
     *
     * @param argumentResolvers
     */
    void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers);

}