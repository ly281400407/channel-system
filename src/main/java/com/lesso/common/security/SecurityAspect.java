package com.lesso.common.security;

import com.lesso.common.exception.TokenException;
import com.lesso.common.util.StringUtil;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;

public class SecurityAspect {

    private static final String DEFAULT_TOKEN_NAME = "User-Token";

    private static final String DEFAULT_USER_NAME = "User-Name";

    private TokenManager tokenManager;
    private String tokenName;
    private String userName;

    public void setTokenManager(TokenManager tokenManager) {
        this.tokenManager = tokenManager;
    }

    public void setTokenName(String tokenName) {
        if (StringUtil.isEmpty(tokenName)) {
            tokenName = DEFAULT_TOKEN_NAME;
        }
        this.tokenName = tokenName;
    }

    public void setUserName(String userName){
        if (StringUtil.isEmpty(userName)) {
            userName = DEFAULT_USER_NAME;;
        }
        this.userName = userName;
    }

    public Object execute(ProceedingJoinPoint pjp) throws Throwable {
        // 从切点上获取目标方法
        MethodSignature methodSignature = (MethodSignature) pjp.getSignature();
        Method method = methodSignature.getMethod();

        // 若目标方法忽略了安全性检查，则直接调用目标方法
        if (method.isAnnotationPresent(IgnoreSecurity.class) && method.getAnnotation(IgnoreSecurity.class).val()) {
            return pjp.proceed();
        }

        // 从 request header 中获取当前 token
        HttpServletRequest request =((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
        String token = request.getHeader(tokenName);
        String username = request.getHeader(userName);

        if(StringUtil.isEmpty(token) || StringUtil.isEmpty(userName)){
            throw new TokenException();
        }

        // 检查 token 有效性
        if (!tokenManager.checkToken(username,token)) {
            String message = String.format("token [%s] is invalid", token);
            throw new TokenException(message);
        }
        // 调用目标方法
        return pjp.proceed();
    }

}
