package cn.bracerframework.core.exception;

/**
 * 应用系统异常
 *
 * @author Dracula
 */
public class SystemException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    /**
     * 应用系统异常
     */
    public SystemException() {
        super();
    }

    /**
     * 应用系统异常
     *
     * @param msg 报错原因
     */
    public SystemException(String msg) {
        super(msg);
    }

    /**
     * 应用系统异常
     *
     * @param msg 报错原因
     * @param e   异常信息
     */
    public SystemException(String msg, Throwable e) {
        super(msg, e);
    }

}