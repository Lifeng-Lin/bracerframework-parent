package cn.bracerframework.core.pojo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 下拉框数据对象
 *
 * @author Dracula
 */
public class ComboBox implements Serializable {
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
     * 是否有效
     */
    private String isValid;

    /**
     * 提示信息
     */
    private String tip;

    /**
     * 级联
     */
    private List<ComboBox> children = new ArrayList<>();

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
     * @return 是否有效
     */
    public String getIsValid() {
        return isValid;
    }

    /**
     * @param isValid 是否有效
     */
    public void setIsValid(String isValid) {
        this.isValid = isValid;
    }

    /**
     * @return 级联
     */
    public List<ComboBox> getChildren() {
        return children;
    }

    /**
     * @param children 级联
     */
    public void setChildren(List<ComboBox> children) {
        this.children = children;
    }

    /**
     * @return 提示信息
     */
    public String getTip() {
        return tip;
    }

    /**
     * @param tip 提示信息
     */
    public void setTip(String tip) {
        this.tip = tip;
    }

}