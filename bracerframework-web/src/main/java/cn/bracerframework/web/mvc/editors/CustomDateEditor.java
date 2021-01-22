package cn.bracerframework.web.mvc.editors;

import cn.bracerframework.core.util.StrUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.NumberUtil;

import java.beans.PropertyEditorSupport;
import java.util.Date;

/**
 * 控制器 (Controller) 时间属性编辑器支持</br>
 * {@link Date} 字段的数值绑定操作
 *
 * @author Lifeng.Lin
 */
public class CustomDateEditor extends PropertyEditorSupport {

    /**
     * 输出时间格式
     */
    protected String outputFormat;
    /**
     * 是否允许为空
     */
    protected boolean allowEmpty;

    /**
     * 时间属性编辑器支持
     *
     * @param outputFormat 输出时间格式
     * @param allowEmpty   是否允许为空
     */
    public CustomDateEditor(String outputFormat, boolean allowEmpty) {
        this.outputFormat = outputFormat;
        this.allowEmpty = allowEmpty;
    }

    /**
     * 时间属性编辑器支持</br>
     * 默认不允许为空
     *
     * @param outputFormat 输出时间格式
     */
    public CustomDateEditor(String outputFormat) {
        this(outputFormat, false);
    }

    /**
     * 时间属性编辑器支持</br>
     * 默认输出格式 yyyy-MM-dd HH:mm:ss
     *
     * @param allowEmpty 是否允许为空
     */
    public CustomDateEditor(boolean allowEmpty) {
        this("yyyy-MM-dd HH:mm:ss", allowEmpty);
    }

    /**
     * 时间属性编辑器支持</br>
     * 默认输出格式 yyyy-MM-dd HH:mm:ss</br>
     * 默认不允许为空
     */
    public CustomDateEditor() {
        this(false);
    }

    /**
     * 获取时间文本
     */
    @Override
    public String getAsText() {
        if (allowEmpty) {
            if (getValue() == null) {
                return "";
            }
        } else {
            if (getValue() == null) {
                throw new IllegalArgumentException("时间对象为空，无法解析为文本");
            }
        }

        return DateUtil.format((Date) getValue(), outputFormat);
    }

    /**
     * 格式化时间文本并赋值
     */
    @Override
    public void setAsText(String text) throws IllegalArgumentException {
        try {
            if (StrUtil.isNotEmpty(text)) {
                setValue(DateUtil.parse(text));
            } else {
                if (allowEmpty) {
                    setValue(null);
                } else {
                    throw new IllegalArgumentException("用于分析的指定文本为空！");
                }
            }
        } catch (Exception e) {
            if (NumberUtil.isNumber(text)) {
                setValue(DateUtil.date(Long.parseLong(text)));
            } else {
                throw new IllegalArgumentException(e.getMessage(), e);
            }
        }
    }

}