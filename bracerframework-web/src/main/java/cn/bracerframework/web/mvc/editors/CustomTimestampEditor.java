package cn.bracerframework.web.mvc.editors;

import cn.bracerframework.core.util.StrUtil;

import java.sql.Timestamp;

/**
 * 控制器 (Controller) 时间属性编辑器支持</br>
 * {@link Timestamp} 格式化
 *
 * @author Lifeng.Lin
 */
public class CustomTimestampEditor extends CustomDateEditor {

    /**
     * 时间属性编辑器支持</br>
     * 默认输出格式 yyyy-MM-dd HH:mm:ss
     *
     * @param allowEmpty 是否允许为空
     */
    public CustomTimestampEditor(boolean allowEmpty) {
        super(allowEmpty);
    }

    /**
     * 时间属性编辑器支持</br>
     * 默认输出格式 yyyy-MM-dd HH:mm:ss</br>
     * 默认不允许为空
     */
    public CustomTimestampEditor() {
        super();
    }

    /**
     * 格式化时间文本并赋值
     */
    @Override
    public void setAsText(String text) throws IllegalArgumentException {
        if (StrUtil.isNotEmpty(text)) {
            try {
                setValue(Timestamp.valueOf(text));
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("无法将文本 [" + text + "] 解析为任何日期格式", e);
            }
        } else {
            if (allowEmpty) {
                setValue(null);
            } else {
                throw new IllegalArgumentException("用于分析的指定文本为空！");
            }
        }
    }

}