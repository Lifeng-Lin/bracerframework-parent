package cn.bracerframework.boot.filter;

import cn.bracerframework.boot.i.AuthorityVerification;
import cn.bracerframework.boot.properties.SecurityProperties;
import cn.bracerframework.core.pojo.OperInfo;
import cn.bracerframework.core.util.AnnotationUtil;
import cn.bracerframework.core.util.ClassUtil;
import cn.bracerframework.core.util.HttpUtil;
import cn.bracerframework.core.util.JsonUtil;
import cn.bracerframework.core.util.StrUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.CharsetUtil;
import cn.hutool.extra.spring.SpringUtil;
import cn.hutool.http.ContentType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Set;

/**
 * 安全访问配置</br>
 * 1、通过实现 {@link AuthorityVerification} 接口的 doVerify 方法进行权限校验</br>
 * 2、通过 bracerframework.security.public-paths 可配置公共访问路径
 *
 * @author Lifeng.Lin
 */
@WebFilter(filterName = "securityFilter", urlPatterns = "/*")
@Order(1)
public class SecurityFilter implements Filter {

    private static Class<?> WX_MP_CONTROLLER = null;
    private static String WX_MP_CONTROLLER_NAME = null;

    static {
        WX_MP_CONTROLLER = ClassUtil.getIfExist("cn.bracerframework.wechat.application.action.WxMpController");
        if (WX_MP_CONTROLLER != null) {
            WX_MP_CONTROLLER_NAME = AnnotationUtil.getAnnotation(WX_MP_CONTROLLER, Controller.class).value();
        }

    }

    @Autowired
    private AuthorityVerification authorityVerification;
    @Autowired
    private SecurityProperties securityProperties;
    /**
     * 请求路径匹配模式
     */
    private PathMatcher matcher;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        matcher = new AntPathMatcher();
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse rep, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) rep;

        String url = getUrl(request);

        if (WX_MP_CONTROLLER != null && isWxMpMapping(url)) {
            chain.doFilter(request, response);
            return;
        }

        Set<String> publicPaths = securityProperties.getPublicPaths();
        // 公用访问路径无需校验登陆状态
        for (String publicPath : publicPaths) {
            if (matcher.match(publicPath, url)) {
                chain.doFilter(request, response);
                return;
            }
        }

        OperInfo operInfo = authorityVerification.doVerify(request, response);

        if (operInfo.getIsOk()) {
            chain.doFilter(request, response);
        } else {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.setContentType(ContentType.JSON.getValue());
            response.setCharacterEncoding(CharsetUtil.defaultCharsetName());
            response.getWriter().write(JsonUtil.toStrContainNull(operInfo));
            response.getWriter().flush();
        }
    }

    /**
     * 是否微信公众号请求映射<br/>
     * 引入 bracerframework-wechat 包并使用 EnableWxMp 注解启用后生效
     *
     * @param url 请求地址
     * @return 是否微信公众号请求映射
     */
    private boolean isWxMpMapping(String url) {
        try {
            SpringUtil.getBean(WX_MP_CONTROLLER_NAME);
            RequestMapping mapping = AnnotationUtil.getAnnotation(WX_MP_CONTROLLER, RequestMapping.class);
            if (ArrayUtil.contains(mapping.value(), url)) {
                return true;
            }
        } catch (Throwable e) {
        }
        return false;
    }

    /**
     * 获取请求地址及参数
     *
     * @param request
     * @return
     */
    private String getUrl(HttpServletRequest request) {
        // 当前请求路径
        String requestUrl = request.getRequestURI();
        String methodParam = HttpUtil.getUrlParameter(request.getQueryString(), "method");
        if (StrUtil.isNotEmpty(methodParam)) {
            requestUrl += "?method=" + methodParam;
        }
        return requestUrl;
    }

    @Override
    public void destroy() {
    }

}