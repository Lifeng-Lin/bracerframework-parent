package cn.bracerframework.core.exception;

/**
 * 用户操作异常
 *
 * @author Lifeng.Lin
 */
public class OperationException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    /**
     * 用户操作异常
     */
    public OperationException() {
        super();
    }

    /**
     * 用户操作异常
     *
     * @param msg 报错原因
     */
    public OperationException(String msg) {
        super(msg);
    }

    /**
     * 用户操作异常
     *
     * @param msg 报错原因
     * @param e   异常信息
     */
    public OperationException(String msg, Throwable e) {
        super(msg, e);
    }

}