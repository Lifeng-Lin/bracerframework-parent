package cn.bracerframework.boot.i;

import cn.bracerframework.core.pojo.OperInfo;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 权限校验接口
 *
 * @author Lifeng.Lin
 */
public interface AuthorityVerification {

    /**
     * 验证登陆状态 未登录返回800状态码
     *
     * @param request
     * @param response
     * @return
     */
    OperInfo doVerify(HttpServletRequest request, HttpServletResponse response);

}