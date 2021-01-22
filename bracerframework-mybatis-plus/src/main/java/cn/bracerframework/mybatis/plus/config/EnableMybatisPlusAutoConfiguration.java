package cn.bracerframework.mybatis.plus.config;

import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import org.springframework.context.annotation.Bean;

/**
 * Mybatis Plus 配置
 *
 * @author Lifeng.Lin
 */
public class EnableMybatisPlusAutoConfiguration {

    /**
     * Mybatis Plus 分页插件
     *
     * @return 分页插件
     */
    @Bean
    public PaginationInterceptor paginationInterceptor() {
        return new PaginationInterceptor();
    }

}