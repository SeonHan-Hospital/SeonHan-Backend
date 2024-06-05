package com.seonhansite.server.sidecar;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
public class ServiceAop {

    @Pointcut("@within(org.springframework.stereotype.Service)")
    private void serviceAnnt(){}

    @Around("serviceAnnt() && execution(* *(..))")
    public Object processCustomAnnotation(ProceedingJoinPoint joinPoint) throws Throwable {

        long start = System.currentTimeMillis();

        try {
            log.info("Service Request: {}", joinPoint.getSignature());
            Object proceed = joinPoint.proceed();
            log.info("Service Response: {}", proceed);
            return proceed;

        } catch (Exception e) {

            log.warn(
                    "Service raise Exception -> signature: {}, message: {}",
                    joinPoint.getSignature(),
                    e.getMessage());
            throw e;

        } finally {
            long finish = System.currentTimeMillis();
            long timeMs = finish - start;
            log.info("Service time: {}ms", timeMs);
        }
    }
}