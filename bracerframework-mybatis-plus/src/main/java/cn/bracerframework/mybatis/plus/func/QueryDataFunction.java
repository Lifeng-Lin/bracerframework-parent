package cn.bracerframework.mybatis.plus.func;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

/**
 * Mybatis Plus 查询用函数
 *
 * @param <P> 查询对象类型
 * @param <R> 返回对象类型
 * @author Lifeng.Lin
 */
@FunctionalInterface
public interface QueryDataFunction<P, R> {

    /**
     * 执行函数
     *
     * @param page  分页对象
     * @param param 查询参数
     * @return 结果集
     */
    IPage<R> apply(Page<R> page, P param);

}