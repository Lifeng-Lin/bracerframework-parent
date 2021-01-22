package cn.bracerframework.core.pojo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 编辑列表提交的数据
 *
 * @param <T> 列表数据类型，作为入参时一定记得写，否则默认使用 HashMap
 * @author Dracula
 */
public class EditGridData<T> implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 新增数据
     */
    private List<T> inserted = new ArrayList<>();

    /**
     * 修改的数据
     */
    private List<T> updated = new ArrayList<>();

    /**
     * 删除的数据
     */
    private List<T> deleted = new ArrayList<>();

    /**
     * @return 新增数据
     */
    public List<T> getInserted() {
        return inserted;
    }

    /**
     * @param inserted 新增数据
     */
    public void setInserted(List<T> inserted) {
        this.inserted = inserted;
    }

    /**
     * @return 修改的数据
     */
    public List<T> getUpdated() {
        return updated;
    }

    /**
     * @param updated 修改的数据
     */
    public void setUpdated(List<T> updated) {
        this.updated = updated;
    }

    /**
     * @return 删除的数据
     */
    public List<T> getDeleted() {
        return deleted;
    }

    /**
     * @param deleted 删除的数据
     */
    public void setDeleted(List<T> deleted) {
        this.deleted = deleted;
    }

}