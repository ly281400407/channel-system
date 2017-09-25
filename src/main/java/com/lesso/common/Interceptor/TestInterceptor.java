package com.lesso.common.Interceptor;

import com.lesso.common.annotation.MyAnnotation;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * Created by czx on 2017/9/21.
 */
@Aspect
@Component
public class TestInterceptor {
    private static final Logger log = LoggerFactory.getLogger(TestInterceptor.class);
    @Pointcut("@annotation(com.lesso.common.annotation.MyAnnotation)")
    public void aopPoint() {
    }
    @Before("aopPoint()")
    public void doRoute(JoinPoint jp) throws Throwable {
        Method method = this.getMethod(jp);
        MyAnnotation doRoute = (MyAnnotation)method.getAnnotation(MyAnnotation.class);
        String routeField = doRoute.routeField();
        System.out.println(routeField);
        Object[] args = jp.getArgs();
        for(Object arg:args){
            System.out.println(arg.toString());
        }
    }

    private Method getMethod(JoinPoint jp) throws NoSuchMethodException {
        Signature sig = jp.getSignature();
        MethodSignature msig = (MethodSignature)sig;
        return this.getClass(jp).getMethod(msig.getName(), msig.getParameterTypes());
    }

    private Class<? extends Object> getClass(JoinPoint jp) throws NoSuchMethodException {
        return jp.getTarget().getClass();
    }
}
