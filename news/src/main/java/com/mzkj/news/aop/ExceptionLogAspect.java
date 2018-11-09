package com.mzkj.news.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Modifier;
import java.util.Date;

@Aspect
@Component
public class ExceptionLogAspect {

//    @AfterReturning(value="execution(* com.mzkj.news.controller.*.*(..))",returning="result")
//    public void after(JoinPoint joinPoint, Object result) throws Throwable {
//        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
//
//        System.out.println("目标方法名为:" + joinPoint.getSignature().getName());
//        System.out.println("目标方法所属类的简单类名:" +        joinPoint.getSignature().getDeclaringType().getSimpleName());
//        System.out.println("目标方法所属类的类名:" + joinPoint.getSignature().getDeclaringTypeName());
//        System.out.println("目标方法声明类型:" + Modifier.toString(joinPoint.getSignature().getModifiers()));
//        System.out.println("目标方法url:https://www.mzkj88.com/mobile" + request.getRequestURI());
//        //获取传入目标方法的参数
//        Object[] args = joinPoint.getArgs();
//        for (int i = 0; i < args.length; i++) {
//            System.out.println("第" + (i+1) + "个参数为:" + args[i]);
//        }
//        System.out.println("被代理的对象:" + joinPoint.getTarget());
//        System.out.println("代理对象自己:" + joinPoint.getThis());
//        String methodName = joinPoint.getSignature().getName();
//        System.out.println("the method 【" + methodName + "】 after");
//    }

    @AfterThrowing(value="execution(* com.mzkj.news.controller.*.*(..))",throwing="e")
    public void afterThorwingMethod(JoinPoint joinPoint, Exception e){
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        System.out.println("目标方法名为:" + joinPoint.getSignature().getName());
        System.out.println("目标方法所属类的简单类名:" +joinPoint.getSignature().getDeclaringType().getSimpleName());
        System.out.println("目标方法所属类的类名:" + joinPoint.getSignature().getDeclaringTypeName());
        System.out.println("目标方法声明类型:" + Modifier.toString(joinPoint.getSignature().getModifiers()));
        System.out.println("目标方法url:https://www.mzkj88.com/mobile" + request.getRequestURI());
        //获取传入目标方法的参数
        Object[] args = joinPoint.getArgs();
        for (int i = 0; i < args.length; i++) {
            System.out.println("第" + (i+1) + "个参数为:" + args[i].toString());
        }
        System.out.println("被代理的对象:" + joinPoint.getTarget());
        System.out.println("代理对象自己:" + joinPoint.getThis());
        String methodName = joinPoint.getSignature().getName();
        Class<? extends Exception> aClass = e.getClass();
        System.out.println("异常类型：" + aClass.getName());
        System.out.println("【异常通知】the method 【" + methodName + "】 occurs exception: " + e.getCause());
        System.out.println("==========================");
    }
}

