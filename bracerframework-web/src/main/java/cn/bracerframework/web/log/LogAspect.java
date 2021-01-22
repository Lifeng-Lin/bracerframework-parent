package cn.bracerframework.web.log;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 日志拦截器
 *
 * @author Lifeng.Lin
 */
@Aspect
public class LogAspect {

    @Autowired
    private LogOperating logOperating;

    @Around("@annotation(cn.bracerframework.web.log.annotation.Log)")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        try {
            logOperating.before(joinPoint);
            Object proceed = joinPoint.proceed();
            logOperating.after(joinPoint, proceed);
            return proceed;
        } catch (Throwable e) {
            logOperating.err(e);
            throw e;
        }
    }

    /**
     * 日志操作接口</br>
     *
     * @author Lifeng.Lin
     */
    public interface LogOperating {

        /**
         * 进入方法前
         *
         * @param joinPoint
         */
        void before(ProceedingJoinPoint joinPoint);

        /**
         * 进入方法后
         *
         * @param joinPoint
         * @param proceed
         */
        void after(ProceedingJoinPoint joinPoint, Object proceed);

        /**
         * 方法报错
         *
         * @param e
         */
        void err(Throwable e);

    }

}