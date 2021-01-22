package cn.bracerframework.core.pojo;

import java.util.List;

/**
 * iView - 菜单树对象
 *
 * @author Dracula
 */
public class IviewMenu extends Tree {
    private static final long serialVersionUID = 1L;

    /**
     * 节点ID
     */
    private String id;

    /**
     * 节点名字
     */
    private String text;

    /**
     * 节点图标class
     */
    private String icon;

    /**
     * 对应页面路径
     */
    private String path;

    /**
     * 子节点
     */
    private List<IviewMenu> children;

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
     * @return 节点名字
     */
    public String getText() {
        return text;
    }

    /**
     * @param text 节点名字
     */
    public void setText(String text) {
        this.text = text;
    }

    /**
     * @return 节点图标class
     */
    public String getIcon() {
        return icon;
    }

    /**
     * @param icon 节点图标class
     */
    public void setIcon(String icon) {
        this.icon = icon;
    }

    /**
     * @return 对应页面路径
     */
    public String getPath() {
        return path;
    }

    /**
     * @param path 对应页面路径
     */
    public void setPath(String path) {
        this.path = path;
    }

    /**
     * @return 子节点
     */
    public List<IviewMenu> getChildren() {
        return children;
    }

    /**
     * @param children 子节点
     */
    public void setChildren(List<IviewMenu> children) {
        this.children = children;
    }

}