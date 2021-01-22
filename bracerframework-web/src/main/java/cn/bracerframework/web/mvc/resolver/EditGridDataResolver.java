package cn.bracerframework.web.mvc.resolver;

import cn.bracerframework.core.pojo.EditGridData;
import cn.bracerframework.core.util.ClassUtil;
import cn.bracerframework.core.util.StrUtil;
import org.springframework.core.MethodParameter;
import org.springframework.lang.Nullable;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.annotation.RequestParamMethodArgumentResolver;
import sun.reflect.generics.reflectiveObjects.ParameterizedTypeImpl;

import java.lang.reflect.Type;

/**
 * 编辑列表参数解析器
 *
 * @author Lifeng.Lin
 */
public class EditGridDataResolver extends RequestParamMethodArgumentResolver {


    public EditGridDataResolver() {
        super(false);
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        if (ClassUtil.isAssignable(parameter.getParameterType(), EditGridData.class)) {
            return true;
        }
        return false;
    }

    @Override
    @Nullable
    protected Object resolveName(String name, MethodParameter parameter, NativeWebRequest request) throws Exception {
        String arg = StrUtil.toString(super.resolveName(name, parameter, request));
        if (StrUtil.isNotEmpty(arg)) {
            Type argType = parameter.getNestedGenericParameterType();
            if (argType != null && argType instanceof ParameterizedTypeImpl) {
                Type[] types = ((ParameterizedTypeImpl) argType).getActualTypeArguments();
                if (types.length == 1) {
                    arg = types[0].getTypeName() + ";" + arg;
                }
            }
        }
        return arg;
    }

}