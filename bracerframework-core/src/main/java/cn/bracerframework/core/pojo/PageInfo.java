package cn.bracerframework.core.pojo;

import java.io.Serializable;

/**
 * 分页对象
 *
 * @author Dracula
 */
public class PageInfo implements Serializable {
    private static final long serialVersionUID = 1L;

    public static String ORDER_ASC = "asc";

    /**
     * 当前页
     */
    private int page = 1;

    /**
     * 每页显示记录数
     */
    private int rows = Integer.MAX_VALUE;

    /**
     * 排序字段名
     */
    private String sort = null;

    /**
     * 按什么排序(asc,desc)
     */
    private String order = ORDER_ASC;

    /**
     * @return 当前页
     */
    public int getPage() {
        return page;
    }

    /**
     * @param page 当前页
     */
    public void setPage(int page) {
        this.page = page;
    }

    /**
     * @return 每页显示记录数
     */
    public int getRows() {
        return rows;
    }

    /**
     * @param rows 每页显示记录数
     */
    public void setRows(int rows) {
        this.rows = rows;
    }

    /**
     * @return 排序字段名
     */
    public String getSort() {
        return sort;
    }

    /**
     * @param sort 排序字段名
     */
    public void setSort(String sort) {
        this.sort = sort;
    }

    /**
     * @return 按什么排序(asc, desc)
     */
    public String getOrder() {
        return order;
    }

    /**
     * @param order 按什么排序(asc, desc)
     */
    public void setOrder(String order) {
        this.order = order;
    }

}