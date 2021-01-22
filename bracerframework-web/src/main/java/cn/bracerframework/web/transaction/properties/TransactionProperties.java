package cn.bracerframework.web.transaction.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Collections;
import java.util.Map;

/**
 * 事务监听配置类
 *
 * @author Lifeng.Lin
 */
@ConfigurationProperties(prefix = TransactionProperties.PREFIX)
public class TransactionProperties {
    public static final String PREFIX = "bracerframework.transaction";

    /**
     * 监听的事务的切入点
     */
    private String expression;
    /**
     * 事务超时时间
     */
    private Integer timeOut = 60000;
    /**
     * 需要监听的事务方法，key为监听的方法名；value为回滚操作对应的异常
     */
    private Map<String, String> methodName = Collections.emptyMap();
    /**
     * 需要监听的事务方法，key为监听指定前缀的方法；value为回滚操作对应的异常
     */
    private Map<String, String> methodNameStart = Collections.emptyMap();

    /**
     * @return 监听的事务的切入点
     */
    public String getExpression() {
        return expression;
    }

    /**
     * @param expression 监听的事务的切入点
     */
    public void setExpression(String expression) {
        this.expression = expression;
    }

    /**
     * @return 事务超时时间
     */
    public int getTimeOut() {
        return timeOut;
    }

    /**
     * @param timeOut 事务超时时间
     */
    public void setTimeOut(int timeOut) {
        this.timeOut = timeOut;
    }

    /**
     * @return 需要监听的事务方法，key为监听的方法名；value为回滚操作对应的异常
     */
    public Map<String, String> getMethodName() {
        return methodName;
    }

    /**
     * @param methodName 需要监听的事务方法，key为监听的方法名；value为回滚操作对应的异常
     */
    public void setMethodName(Map<String, String> methodName) {
        this.methodName = methodName;
    }

    /**
     * @return 需要监听的事务方法，key为监听指定前缀的方法；value为回滚操作对应的异常
     */
    public Map<String, String> getMethodNameStart() {
        return methodNameStart;
    }

    /**
     * @param methodNameStart 需要监听的事务方法，key为监听指定前缀的方法；value为回滚操作对应的异常
     */
    public void setMethodNameStart(Map<String, String> methodNameStart) {
        this.methodNameStart = methodNameStart;
    }

}