package cn.bracerframework.mybatis.plus;

import cn.bracerframework.core.pojo.DataGrid;
import cn.bracerframework.core.pojo.PageInfo;
import cn.bracerframework.core.util.BeanUtil;
import cn.bracerframework.core.util.StrUtil;
import cn.bracerframework.mybatis.plus.func.QueryDataFunction;
import cn.hutool.core.util.ReflectUtil;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.enums.SqlMethod;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.ibatis.binding.MapperMethod;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.lang.reflect.Field;
import java.util.Collection;

/**
 * Mybatis Plus 操作数据库基础服务
 *
 * @author Lifeng.Lin
 */
public class MybatisPlusService<M extends BaseMapper<T>, T> extends ServiceImpl<M, T> {

    protected Logger log = LogManager.getLogger(this.getClass());

    /**
     * 分页查询
     *
     * @param func     查询函数：(page, param) -> this.baseMapper.getXxxList(page, param)
     * @param param    查询参数
     * @param pageInfo 分页对象
     * @param <P>      查询对象类型
     * @param <R>      返回对象类型
     * @return
     */
    public <P, R> DataGrid<R> queryDataGrid(QueryDataFunction<P, R> func, P param, PageInfo pageInfo) {
        Page<R> page = new Page<>(pageInfo.getPage(), pageInfo.getRows());
        if (StrUtil.isNotEmpty(pageInfo.getSort())) {
            if (StrUtil.equalsIgnoreCase(PageInfo.ORDER_ASC, pageInfo.getOrder())) {
                page.addOrder(OrderItem.asc(pageInfo.getSort()));
            } else {
                page.addOrder(OrderItem.desc(pageInfo.getSort()));
            }
        }
        IPage<R> iPage = func.apply(page, param);
        DataGrid<R> dataGrid = new DataGrid<>();
        dataGrid.setRows(iPage.getRecords());
        dataGrid.setTotal(page.getTotal());
        return dataGrid;
    }

    public boolean updateBatch(Collection<T> entityList, String byFields) {
        return updateBatch(entityList, byFields, 1000);
    }

    public boolean updateBatch(Collection<T> entityList, String byFields, int batchSize) {
        String sqlStatement = sqlStatement(SqlMethod.UPDATE);
        String[] fields = byFields.split(",");

        return executeBatch(entityList, batchSize, (sqlSession, entity) -> {
            Field f = null;
            TableId tableId = null;
            TableField tableField = null;
            QueryWrapper queryWrapper = null;

            MapperMethod.ParamMap<Object> param = new MapperMethod.ParamMap<>();
            param.put(Constants.ENTITY, entity);
            queryWrapper = new QueryWrapper<T>();
            for (String field : fields) {
                f = ReflectUtil.getField(entity.getClass(), field);
                if (f == null) {
                    throw new RuntimeException(entity.getClass().getSimpleName() + " 字段 " + field + " 不存在！");
                }
                if ((tableId = f.getAnnotation(TableId.class)) != null && StrUtil.isNotEmpty(tableId.value())) {
                    queryWrapper.eq(tableId.value(), BeanUtil.getFieldValue(entity, field));
                } else if ((tableField = f.getAnnotation(TableField.class)) != null && StrUtil.isNotEmpty(tableField.value())) {
                    queryWrapper.eq(tableField.value(), BeanUtil.getFieldValue(entity, field));
                } else {
                    queryWrapper.eq(StrUtil.toUnderlineCase(field), BeanUtil.getFieldValue(entity, field));
                }
            }
            param.put(Constants.WRAPPER, queryWrapper);
            sqlSession.update(sqlStatement, param);
        });
    }

    public boolean removeBatch(Collection<T> entityList, String byFields) {
        return removeBatch(entityList, byFields, 1000);
    }

    public boolean removeBatch(Collection<T> entityList, String byFields, int batchSize) {
        String sqlStatement = sqlStatement(SqlMethod.DELETE);
        String[] fields = byFields.split(",");

        return executeBatch(entityList, batchSize, (sqlSession, entity) -> {
            Field f = null;
            TableId tableId = null;
            TableField tableField = null;
            QueryWrapper queryWrapper = null;

            MapperMethod.ParamMap<Object> param = new MapperMethod.ParamMap<>();
            param.put(Constants.ENTITY, entity);
            queryWrapper = new QueryWrapper<T>();
            for (String field : fields) {
                f = ReflectUtil.getField(entity.getClass(), field);
                if (f == null) {
                    throw new RuntimeException(entity.getClass().getSimpleName() + " 字段 " + field + " 不存在！");
                }
                if ((tableId = f.getAnnotation(TableId.class)) != null && StrUtil.isNotEmpty(tableId.value())) {
                    queryWrapper.eq(tableId.value(), BeanUtil.getFieldValue(entity, field));
                } else if ((tableField = f.getAnnotation(TableField.class)) != null && StrUtil.isNotEmpty(tableField.value())) {
                    queryWrapper.eq(tableField.value(), BeanUtil.getFieldValue(entity, field));
                } else {
                    queryWrapper.eq(StrUtil.toUnderlineCase(field), BeanUtil.getFieldValue(entity, field));
                }
            }
            param.put(Constants.WRAPPER, queryWrapper);
            sqlSession.delete(sqlStatement, param);
        });
    }

}