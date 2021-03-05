package com.example.listener.log;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

@Aspect
@Component
@Slf4j
public class LogAspect {
    @Before("execution(* com.example.listener.Service.Handler.*(..))")
    public void before1(JoinPoint joinPoint){
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        log.info("Handler " + method.getName() + " hand success");
    }

    @Before("execution(* com.example.listener.Listener.ListenerService.*(..))")
    public void before2(JoinPoint joinPoint){
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        log.info("Listener " + method.getName() + " receive message success");
    }
}