package com.ajaxjs.security;

import com.ajaxjs.security.captcha.google.GoogleCaptcha;
import com.ajaxjs.security.captcha.google.GoogleCaptchaCheck;
import com.ajaxjs.security.limit.LimitAccess;
import com.ajaxjs.security.limit.LimitAccessVerify;
import com.ajaxjs.security.nonrepeatsubmit.NonRepeatSubmit;
import com.ajaxjs.security.nonrepeatsubmit.NonRepeatSubmitMgr;
import com.ajaxjs.security.referer.HttpReferer;
import com.ajaxjs.security.referer.HttpRefererCheck;
import com.ajaxjs.security.common.SpringUtils;
import com.ajaxjs.security.timesignature.TimeSignature;
import com.ajaxjs.security.timesignature.TimeSignatureVerify;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

@Slf4j
public class SecurityFilter implements HandlerInterceptor {
    @Autowired(required = false)
    NonRepeatSubmitMgr nonRepeatSubmitMgr;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (!(handler instanceof HandlerMethod))
            return true;

        HandlerMethod handlerMethod = (HandlerMethod) handler; // 接口上的方法
        Method method = handlerMethod.getMethod();

        if (nonRepeatSubmitMgr != null && method.isAnnotationPresent(NonRepeatSubmit.class))
            nonRepeatSubmitMgr.handle(request, method);

        if (!handler(TimeSignature.class, request, handlerMethod, method, TimeSignatureVerify.class))
            return false;

        if (!handler(GoogleCaptcha.class, request, handlerMethod, method, GoogleCaptchaCheck.class))
            return false;

        if (!handler(LimitAccess.class, request, handlerMethod, method, LimitAccessVerify.class))
            return false;

        if (!handler(HttpReferer.class, request, handlerMethod, method, HttpRefererCheck.class))
            return false;

        return true;
    }

    /**
     * 处理拦截器动作的方法
     * 该方法用于根据给定的拦截器服务类、HTTP 请求、处理方法和方法注解类型，执行相应的拦截器动作
     *
     * @param <T>            扩展 Annotation 的类型
     * @param serviceClz     拦截器服务类的类型，必须是 InterceptorAction 的子类
     * @param req            HTTP 请求对象，用于拦截器动作中可能需要的请求信息
     * @param handlerMethod  处理方法对象，用于获取方法上的注解信息
     * @param method         具体的 Java 方法对象，用于获取方法上的注解信息
     * @param annotationType 注解的类型，用于查找相应的方法或类注解
     * @return boolean 表示是否继续执行其他拦截器动作或放行请求
     */
    @SuppressWarnings("unchecked")
    private <T extends Annotation> boolean handler(Class<? extends InterceptorAction<T>> serviceClz, HttpServletRequest req,
                                                   HandlerMethod handlerMethod, Method method, Class<T> annotationType) {
        InterceptorAction<T> service = SpringUtils.getBean(serviceClz);// 获取拦截器服务的实例

        if (service == null)// 如果服务实例为空，表示对应的业务没有创建，直接放行
            return true;

        Annotation annotation = null;

        if (service.isGlobalCheck()) {// 如果服务配置了全局检查，则不需要查找方法或类注解
            // TODO: 当前缺乏一个获取配置的手段，需要进一步实现
        } else {
            // 获取处理方法的Bean类型，并尝试获取类级别的注解
            Class<?> beanType = handlerMethod.getBeanType(); // or method.getDeclaringClass()
            Annotation classAnnotation = beanType.getAnnotation(annotationType);

            if (classAnnotation != null)  // 如果类级别的注解存在，则使用该注解
                annotation = classAnnotation;
            else {
                Annotation interfaceAnnotation = handlerMethod.getMethodAnnotation(annotationType);  // 尝试获取方法级别的注解，包括接口上的注解

                if (interfaceAnnotation != null) // 如果接口上的注解存在，则使用该注解
                    annotation = interfaceAnnotation;
                else
                    annotation = method.getAnnotation(annotationType);  // 最后尝试获取具体方法上的注解
            }
        }


        if (annotation != null) {        // 如果注解存在，执行拦截器动作
            T a = (T) annotation;
            log.info(String.valueOf(a));

            return service.action(a, req);
        }

        return true;  // 如果注解不存在，直接放行
    }

}
