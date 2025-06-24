package com.ajaxjs.security;

import com.ajaxjs.security.nonrepeatsubmit.NonRepeatSubmit;
import com.ajaxjs.security.nonrepeatsubmit.NonRepeatSubmitMgr;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

public class SecurityFilter extends HandlerInterceptorAdapter {
    @Autowired(required = false)
    NonRepeatSubmitMgr nonRepeatSubmitMgr;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws  Exception {
        Method method = getMethod(handler);

        if (nonRepeatSubmitMgr != null && method.isAnnotationPresent(NonRepeatSubmit.class))
            nonRepeatSubmitMgr.handle(request, method);


        return true;
    }

    static Method getMethod(Object handler) {
        return ((HandlerMethod) handler).getMethod();
    }
}
