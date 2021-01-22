package cn.bracerframework.web.mvc.binding;

import cn.bracerframework.core.pojo.EditGridData;
import cn.bracerframework.web.mvc.editors.CustomDateEditor;
import cn.bracerframework.web.mvc.editors.CustomTimestampEditor;
import cn.bracerframework.web.mvc.editors.EditGridDataEditor;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.sql.Timestamp;
import java.util.Date;

/**
 * 全局 Controller 消息处理
 *
 * @author Lifeng.Lin
 */
@RestControllerAdvice
public class ControllerAdviceHandler {

    /**
     * 处理请求中数据绑定</br>
     * 默认已实现：</br>
     * 1、对字符串类型了进行 trim，空字符串不会被转成 null {@link StringTrimmerEditor}</br>
     * 2、时间型字符串转 Timestamp {@link CustomTimestampEditor}</br>
     * 3、日期型字符串转 Date {@link CustomDateEditor}</br>
     * 4、编辑列表提交数据字符串转 EditGridData {@link EditGridDataEditor}</br>
     *
     * @param binder
     */
    @InitBinder
    public void initBinder(ServletRequestDataBinder binder) {
        binder.registerCustomEditor(EditGridData.class, new EditGridDataEditor());
        binder.registerCustomEditor(String.class, new StringTrimmerEditor(false));
        binder.registerCustomEditor(Timestamp.class, new CustomTimestampEditor(true));
        binder.registerCustomEditor(Date.class, new CustomDateEditor(true));
    }

}