package cn.bracerframework.web.transaction.config;

import cn.bracerframework.core.util.StrUtil;
import cn.bracerframework.web.transaction.properties.TransactionProperties;
import org.springframework.aop.Advisor;
import org.springframework.aop.aspectj.AspectJExpressionPointcut;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionManager;
import org.springframework.transaction.interceptor.NameMatchTransactionAttributeSource;
import org.springframework.transaction.interceptor.RollbackRuleAttribute;
import org.springframework.transaction.interceptor.RuleBasedTransactionAttribute;
import org.springframework.transaction.interceptor.TransactionAttribute;
import org.springframework.transaction.interceptor.TransactionInterceptor;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * 数据库事务配置
 *
 * @author Lifeng.Lin
 */
@EnableConfigurationProperties(value = {TransactionProperties.class})
public class TransactionAutoConfiguration {

    /**
     * 事务管理配置
     *
     * @param transactionManager    事务管理器
     * @param transactionProperties 事务监听配置
     * @return 事务切面
     */
    @Bean
    public TransactionInterceptor txAdvice(TransactionManager transactionManager, TransactionProperties transactionProperties) {
        /* 只读事务，不做更新操作 */
        RuleBasedTransactionAttribute readOnly = new RuleBasedTransactionAttribute();
        readOnly.setReadOnly(true);
        readOnly.setPropagationBehavior(TransactionDefinition.PROPAGATION_NOT_SUPPORTED);
        /* 当前存在事务就使用当前事务，当前不存在事务就创建一个新的事务 */
        RuleBasedTransactionAttribute required = new RuleBasedTransactionAttribute();
        required.setRollbackRules(Collections.singletonList(new RollbackRuleAttribute(Exception.class)));
        required.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
        required.setReadOnly(false);
        required.setTimeout(transactionProperties.getTimeOut());

        Map<String, String> methodName = transactionProperties.getMethodName();
        if (methodName == null) {
            methodName = new HashMap<>(0);
        }

        Map<String, String> methodNameStart = transactionProperties.getMethodNameStart();
        if (methodNameStart == null) {
            methodNameStart = new HashMap<>(0);
        }

        Map<String, TransactionAttribute> txAdviceMap = new HashMap<>(6 + methodName.size() + methodNameStart.size());
        txAdviceMap.put("add*", required);
        txAdviceMap.put("save*", required);
        txAdviceMap.put("insert*", required);
        txAdviceMap.put("update*", required);
        txAdviceMap.put("delete*", required);
        txAdviceMap.put("*", readOnly);

        for (String key : methodName.keySet()) {
            required = new RuleBasedTransactionAttribute();
            required.setRollbackRules(Collections.singletonList(new RollbackRuleAttribute(methodName.get(key))));
            required.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
            required.setReadOnly(false);
            required.setTimeout(transactionProperties.getTimeOut());
            txAdviceMap.put(key, required);
        }

        for (String key : methodNameStart.keySet()) {
            required = new RuleBasedTransactionAttribute();
            required.setRollbackRules(Collections.singletonList(new RollbackRuleAttribute(methodNameStart.get(key))));
            required.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
            required.setReadOnly(false);
            required.setTimeout(transactionProperties.getTimeOut());
            txAdviceMap.put(key + "*", required);
        }

        NameMatchTransactionAttributeSource source = new NameMatchTransactionAttributeSource();
        source.setNameMap(txAdviceMap);

        return new TransactionInterceptor(transactionManager, source);
    }

    /**
     * 事务切点配置
     *
     * @param txAdvice              事务切面
     * @param transactionProperties 事务监听配置
     * @return 事务切点配置
     */
    @Bean
    public Advisor txAdviceAdvisor(TransactionInterceptor txAdvice, TransactionProperties transactionProperties) {
        String expression = transactionProperties.getExpression();
        if (StrUtil.isNotEmpty(expression)) {
            AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();
            pointcut.setExpression(expression);
            return new DefaultPointcutAdvisor(pointcut, txAdvice);
        } else {
            return new DefaultPointcutAdvisor(txAdvice);
        }
    }

}