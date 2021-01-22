package cn.bracerframework.core.pojo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 列表数据对象
 *
 * @author Dracula
 */
public class DataGrid<T> implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 每行数据
     */
    private List<T> rows;

    /**
     * 总记录数
     */
    private Long total;

    /**
     * 尾部统计数据
     */
    private Object footer;

    /**
     * 初始化对象
     */
    public DataGrid() {
        this(new ArrayList<>(), 0L, null);
    }

    /**
     * 初始化对象
     *
     * @param rows  每行记录
     * @param total 总记录数
     */
    public DataGrid(List<T> rows, Long total) {
        this(rows, total, null);
    }

    /**
     * 初始化对象
     *
     * @param rows   每行记录
     * @param total  总记录数
     * @param footer 尾部记录
     */
    public DataGrid(List<T> rows, Long total, Object footer) {
        this.rows = rows;
        this.total = total;
        this.footer = footer;
    }

    /**
     * @return 每行数据
     */
    public List<T> getRows() {
        return rows;
    }

    /**
     * @param rows 每行数据
     */
    public void setRows(List<T> rows) {
        this.rows = rows;
    }

    /**
     * @return 总记录数
     */
    public Long getTotal() {
        return total;
    }

    /**
     * @param total 总记录数
     */
    public void setTotal(Long total) {
        this.total = total;
    }

    /**
     * @return 尾部统计数据
     */
    @SuppressWarnings("unchecked")
    public <V> V getFooter() {
        return (V) footer;
    }

    /**
     * @param footer 尾部统计数据
     */
    public <V> void setFooter(V footer) {
        this.footer = footer;
    }

}