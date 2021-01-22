package cn.bracerframework.web.mvc.exception;

import cn.bracerframework.core.exception.OperationException;
import cn.bracerframework.core.exception.SystemException;
import cn.bracerframework.core.pojo.OperInfo;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 全局异常消息处理
 *
 * @author Lifeng.Lin
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    protected Logger logger = LogManager.getLogger(this.getClass());

    /**
     * 操作异常处理 {@link OperationException}
     *
     * @param e
     * @return 错误信息
     */
    @ExceptionHandler(OperationException.class)
    public OperInfo handleOperationException(OperationException e) {
        logger.error(e.getMessage(), e);
        return OperInfo.err(e.getMessage());
    }

    /**
     * 系统异常处理 {@link SystemException}
     *
     * @param e
     * @return 错误信息
     */
    @ExceptionHandler(SystemException.class)
    public OperInfo handleSystemException(SystemException e) {
        logger.error(e.getMessage(), e);
        return OperInfo.err("系统异常，请联系系统管理员！");
    }

    /**
     * 系统异常处理 {@link Exception}
     *
     * @param e
     * @return 错误信息
     */
    @ExceptionHandler(Exception.class)
    public OperInfo handleException(Exception e) {
        logger.error(e.getMessage(), e);
        return OperInfo.err("系统异常，请联系系统管理员！");
    }

}