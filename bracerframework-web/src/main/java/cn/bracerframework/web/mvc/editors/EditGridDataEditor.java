package cn.bracerframework.web.mvc.editors;

import cn.bracerframework.core.pojo.EditGridData;
import cn.bracerframework.core.util.ClassUtil;
import cn.bracerframework.core.util.JsonUtil;
import cn.bracerframework.core.util.StrUtil;

import java.beans.PropertyEditorSupport;
import java.util.HashMap;

/**
 * 控制器 (Controller) 编辑列表数据编辑器支持</br>
 * {@link EditGridData} 字段的数值绑定操作
 *
 * @author Lifeng.Lin
 */
public class EditGridDataEditor extends PropertyEditorSupport {

    @Override
    public String getAsText() {
        return JsonUtil.toStrContainNull(getValue());
    }

    /**
     * 格式化编辑列表文本并赋值
     */
    @Override
    public void setAsText(String text) throws IllegalArgumentException {
        if (StrUtil.isNotEmpty(text)) {
            String type = StrUtil.subBefore(text, ";", false);
            String value = StrUtil.subAfter(text, ";", false);
            Object val = null;
            if (StrUtil.isEmpty(type)) {
                val = JsonUtil.parseGenericObj(value, EditGridData.class, HashMap.class);
            } else {
                val = JsonUtil.parseGenericObj(value, EditGridData.class, ClassUtil.loadClass(type));
            }

            setValue(val);
        } else {
            setValue(new EditGridData<>());
        }
    }

}