package cn.bracerframework.web.permissions;

import cn.bracerframework.core.exception.OperationException;
import cn.bracerframework.core.util.AnnotationUtil;
import cn.bracerframework.web.permissions.annotation.RequiresPermissions;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Set;

/**
 * 访问权限控制拦截
 *
 * @author Lifeng.Lin
 */
@Aspect
public class PermissionsAspect {
    protected Logger logger = LogManager.getLogger(this.getClass());

    @Autowired(required = false)
    private AuthorizingDataSource authorizingDataSource;

    @Around("@annotation(cn.bracerframework.web.permissions.annotation.RequiresPermissions)")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        if (authorizingDataSource == null) {
            logger.warn("无法加载权限数据源，RequiresPermissions 注解无法使用，请检查是否已实现 AuthorizingDataSource 接口，并实例化到 spring 容器中！");
        } else {
            boolean haveAuthority = false;
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            Set<String> authSet = authorizingDataSource.doGetAuthorizationInfo(attributes.getRequest());

            if (authSet == null) {
                haveAuthority = true;
                logger.warn("权限数据源 AuthorizingDataSourceI.doGetAuthorizationInfo 返回 null，将跳过权限校验！");
            } else {
                String[] permissions = AnnotationUtil.getAnnotationValue(((MethodSignature) joinPoint.getSignature()).getMethod(), RequiresPermissions.class, "value");
                for (String permission : permissions) {
                    if (authSet.contains(permission)) {
                        haveAuthority = true;
                        break;
                    }
                }
            }
            if (!haveAuthority) {
                throw new OperationException("您当前没有权限进行该操作！");
            }
        }

        return joinPoint.proceed();
    }

    /**
     * 权限数据源接口类</br>
     *
     * @author Lifeng.Lin
     */
    public interface AuthorizingDataSource {

        /**
         * 获取用于授权的数据<br/>
         * 返回 null，将跳过权限校验
         *
         * @param request
         * @return 用于授权的数据
         */
        Set<String> doGetAuthorizationInfo(HttpServletRequest request);

    }

}