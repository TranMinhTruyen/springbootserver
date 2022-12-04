package com.ggapp.common.utils;

import com.ggapp.common.exception.ApplicationException;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

@Aspect
@Component
public class AspectClass {
    private static final Logger LOGGER = LoggerFactory.getLogger(AspectClass.class);

    @Autowired
    private PlatformTransactionManager transactionManager;

    @Around(value = "execution(* com.ggapp.controller.*.*(..))", argNames = "joinPoint")
    public Object controllerLogger(ProceedingJoinPoint joinPoint) throws ApplicationException {
        Object value = null;
        long startTime = System.currentTimeMillis();
        final String controller = joinPoint.getTarget().getClass().getName() + "." + joinPoint.getSignature().getName();
        LOGGER.info("Start time taken by controller: {}", controller);
        try {
            value = joinPoint.proceed();
            LOGGER.info("Run controller: {}", controller);
        } catch (ApplicationException e) {
            LOGGER.error("Controller name {} has error: {}", controller, e.getMessage());
            throw new ApplicationException(e.getMessage(), e.getErrorCode());
        } catch (Throwable e) {
            LOGGER.error("Controller name {} has error: {}", controller, e.getMessage());
            throw new ApplicationException(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        } finally {
            Long timeTaken = System.currentTimeMillis() - startTime;
            LOGGER.info("End {} time taken {} ms", controller, timeTaken);
        }
        return value;
    }

    @Around(value = "execution(* com.ggapp.common.*.*(..))", argNames = "joinPoint")
    public Object commonLogger(ProceedingJoinPoint joinPoint) throws ApplicationException {
        Object value = null;
        long startTime = System.currentTimeMillis();
        final String method = joinPoint.getTarget().getClass().getName() + "." + joinPoint.getSignature().getName();
        LOGGER.info("Start time taken by controller: {}", method);
        try {
            value = joinPoint.proceed();
            LOGGER.info("Run controller: {}", method);
        } catch (ApplicationException e) {
            LOGGER.error("Controller name {} has error: {}", method, e.getMessage());
            throw new ApplicationException(e.getMessage(), e.getErrorCode());
        } catch (Throwable e) {
            LOGGER.error("Controller name {} has error: {}", method, e.getMessage());
            throw new ApplicationException(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        } finally {
            Long timeTaken = System.currentTimeMillis() - startTime;
            LOGGER.info("End {} time taken {} ms", method, timeTaken);
        }
        return value;
    }

    @Around(value = "execution(* com.ggapp.services.*.*(..))", argNames = "joinPoint")
    public Object around(ProceedingJoinPoint joinPoint) throws ApplicationException {
        long startTime = System.currentTimeMillis();
        final String method = joinPoint.getTarget().getClass().getName() + "." + joinPoint.getSignature().getName();
        LOGGER.info("Start time taken by method: {}", method);
        DefaultTransactionDefinition definition = new DefaultTransactionDefinition();
        definition.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
        TransactionStatus transactionStatus = transactionManager.getTransaction(definition);
        Object value = null;
        try {
            value = joinPoint.proceed();
            transactionManager.commit(transactionStatus);
            LOGGER.info("Do commit");
        } catch (ApplicationException e) {
            transactionManager.rollback(transactionStatus);
            LOGGER.info("Do rollback");
            LOGGER.error(e.getMessage());
            throw new ApplicationException(e.getMessage(), e.getErrorCode());
        } catch (Throwable e) {
            LOGGER.info("Do rollback");
            LOGGER.error("Method name {} has error: {}", method, e.getMessage());
            throw new ApplicationException(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        } finally {
            Long timeTaken = System.currentTimeMillis() - startTime;
            LOGGER.info("End {} time taken {} ms", method, timeTaken);
        }
        return value;
    }
}
