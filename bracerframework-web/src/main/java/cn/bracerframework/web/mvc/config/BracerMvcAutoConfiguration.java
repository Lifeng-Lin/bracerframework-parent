package cn.bracerframework.web.mvc.config;

import cn.bracerframework.core.util.StrUtil;
import cn.bracerframework.web.mvc.binding.ControllerAdviceHandler;
import cn.bracerframework.web.mvc.exception.GlobalExceptionHandler;
import cn.bracerframework.web.mvc.properties.FileUploadProperties;
import cn.bracerframework.web.mvc.resolver.EditGridDataResolver;
import cn.bracerframework.web.mvc.resolver.i.CustomResolver;
import cn.hutool.extra.spring.SpringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.FileSystemResource;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

import java.io.IOException;
import java.util.List;

/**
 * 定制 MVC 配置类
 *
 * @author Lifeng.Lin
 */
@EnableConfigurationProperties(value = {FileUploadProperties.class})
public class BracerMvcAutoConfiguration extends WebMvcConfigurationSupport {

    @Autowired(required = false)
    private CustomResolver customResolver;

    /**
     * Spring 上下文操作工具
     *
     * @return
     */
    @Bean
    @ConditionalOnMissingBean(SpringUtil.class)
    public SpringUtil springUtil() {
        return new SpringUtil();
    }

    @Bean
    @ConditionalOnMissingBean(ControllerAdviceHandler.class)
    public ControllerAdviceHandler controllerAdviceHandler() {
        return new ControllerAdviceHandler();
    }

    @Bean
    @ConditionalOnMissingBean(GlobalExceptionHandler.class)
    public GlobalExceptionHandler globalExceptionHandler() {
        return new GlobalExceptionHandler();
    }

    @Override
    protected void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        argumentResolvers.add(new EditGridDataResolver());

        if (customResolver != null) {
            customResolver.addArgumentResolvers(argumentResolvers);
        }
    }

    /**
     * 文件上传配置
     *
     * @param webApplicationContext
     * @param properties            {@link FileUploadProperties}
     * @return
     */
    @Bean
    public MultipartResolver multipartResolver(WebApplicationContext webApplicationContext, FileUploadProperties properties) {
        CommonsMultipartResolver resolver = new CommonsMultipartResolver();
        resolver.setDefaultEncoding(properties.getEncoding());
        resolver.setPreserveFilename(properties.isPreserveFilename());
        resolver.setResolveLazily(properties.isResolveLazily());
        resolver.setMaxInMemorySize(properties.getMaxInMemorySize());
        resolver.setMaxUploadSizePerFile(properties.getMaxUploadSizePerFile());
        resolver.setMaxUploadSize(properties.getMaxUploadSize());
        if (StrUtil.isEmpty(properties.getUploadTempDir())) {
            resolver.setServletContext(webApplicationContext.getServletContext());
        } else {
            try {
                resolver.setUploadTempDir(new FileSystemResource(properties.getUploadTempDir()));
            } catch (IOException e) {
                resolver.setServletContext(webApplicationContext.getServletContext());
            }
        }
        return resolver;
    }

}