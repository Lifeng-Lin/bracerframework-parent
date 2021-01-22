package cn.bracerframework.core.pojo;

import java.util.List;

/**
 * iView - 树对象
 *
 * @author Dracula
 */
public class IviewTree extends Tree {
    private static final long serialVersionUID = 1L;

    /**
     * 节点ID
     */
    private String id;

    /**
     * 节点标题
     */
    private String title;

    /**
     * 是否勾选状态
     */
    private boolean checked = false;

    /**
     * 是否展开状态
     */
    private boolean expand = true;

    /**
     * 是否禁用响应
     */
    private boolean disabled = false;

    /**
     * 节点数据
     */
    private Object extra;

    /**
     * 子节点
     */
    private List<IviewTree> children;

    /**
     * @return 节点ID
     */
    public String getId() {
        return id;
    }

    /**
     * @param id 节点ID
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * @return 节点标题
     */
    public String getTitle() {
        return title;
    }

    /**
     * @param title 节点标题
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * @return 是否勾选状态
     */
    public boolean isChecked() {
        return checked;
    }

    /**
     * @param checked 是否勾选状态
     */
    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    /**
     * @return 是否展开状态
     */
    public boolean isExpand() {
        return expand;
    }

    /**
     * @param expand 是否展开状态
     */
    public void setExpand(boolean expand) {
        this.expand = expand;
    }

    /**
     * @return 是否禁用响应
     */
    public boolean isDisabled() {
        return disabled;
    }

    /**
     * @param disabled 是否禁用响应
     */
    public void setDisabled(boolean disabled) {
        this.disabled = disabled;
    }

    /**
     * @return 节点数据
     */
    @SuppressWarnings("unchecked")
    public <T> T getExtra() {
        return (T) extra;
    }

    /**
     * @param extra 节点数据
     */
    public <T> void setExtra(T extra) {
        this.extra = extra;
    }

    /**
     * @return 子节点
     */
    public List<IviewTree> getChildren() {
        return children;
    }

    /**
     * @param children 子节点
     */
    public void setChildren(List<IviewTree> children) {
        this.children = children;
    }

}