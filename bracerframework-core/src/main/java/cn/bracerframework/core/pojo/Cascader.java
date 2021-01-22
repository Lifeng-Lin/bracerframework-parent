package cn.bracerframework.core.pojo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 级联选择数据对象
 *
 * @author Dracula
 */
public class Cascader implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 值
     */
    private String value;

    /**
     * 标签
     */
    private String label;

    /**
     * 子集
     */
    private List<Cascader> children = new ArrayList<>();

    /**
     * @return 值
     */
    public String getValue() {
        return value;
    }

    /**
     * @param value 值
     */
    public void setValue(String value) {
        this.value = value;
    }

    /**
     * @return 标签
     */
    public String getLabel() {
        return label;
    }

    /**
     * @param label 标签
     */
    public void setLabel(String label) {
        this.label = label;
    }

    /**
     * @return 子集
     */
    public List<Cascader> getChildren() {
        return children;
    }

    /**
     * @param children 子集
     */
    public void setChildren(List<Cascader> children) {
        this.children = children;
    }

}