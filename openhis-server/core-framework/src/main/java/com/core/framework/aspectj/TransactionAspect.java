package com.core.framework.aspectj;

import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

/**
 * 事务处理
 */
@Aspect
@Component
public class TransactionAspect {

    private final PlatformTransactionManager transactionManager;
    private final ThreadLocal<TransactionStatus> transactionStatus = new ThreadLocal<>();

    public TransactionAspect(PlatformTransactionManager transactionManager) {
        this.transactionManager = transactionManager;
    }

    @Before("@annotation(org.springframework.web.bind.annotation.PostMapping) || " +
            "@annotation(org.springframework.web.bind.annotation.GetMapping) || " +
            "@annotation(org.springframework.web.bind.annotation.PutMapping) || " +
            "@annotation(org.springframework.web.bind.annotation.DeleteMapping)")
    public void beginTransaction() {
        TransactionStatus status = transactionManager.getTransaction(new DefaultTransactionDefinition());
        transactionStatus.set(status);
    }

    @AfterReturning("@annotation(org.springframework.web.bind.annotation.PostMapping) || " +
            "@annotation(org.springframework.web.bind.annotation.GetMapping) || " +
            "@annotation(org.springframework.web.bind.annotation.PutMapping) || " +
            "@annotation(org.springframework.web.bind.annotation.DeleteMapping)")
    public void commitTransaction() {
        TransactionStatus status = transactionStatus.get();
        if (status != null && !status.isCompleted()) {
            transactionManager.commit(status);
            transactionStatus.remove(); // 清除 ThreadLocal 中的状态
        }
    }

    @AfterThrowing(pointcut = "@annotation(org.springframework.web.bind.annotation.PostMapping) || " +
            "@annotation(org.springframework.web.bind.annotation.GetMapping) || " +
            "@annotation(org.springframework.web.bind.annotation.PutMapping) || " +
            "@annotation(org.springframework.web.bind.annotation.DeleteMapping)",
            throwing = "ex")
    public void rollbackTransaction(Exception ex) {
        TransactionStatus status = transactionStatus.get();
        if (status != null && !status.isCompleted()) {
            transactionManager.rollback(status);
            transactionStatus.remove(); // 清除 ThreadLocal 中的状态
        }
    }
}