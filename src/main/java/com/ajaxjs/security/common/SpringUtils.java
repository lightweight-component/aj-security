package com.ajaxjs.security.common;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

@Slf4j
public class SpringUtils implements ApplicationContextAware {
    public static HttpServletRequest getRequest() {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        return ((ServletRequestAttributes) requestAttributes).getRequest();
    }

    /**
     * Spring 上下文
     */
    public static ApplicationContext context;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        SpringUtils.context = applicationContext;
    }

    /**
     * 获取已注入的对象
     *
     * @param <T> 对象类型
     * @param clz 对象类型引用
     * @return 组件对象
     */
    public static <T> T getBean(Class<T> clz) {
        if (context == null) {
            log.warn("Spring Bean 未准备好，不能返回 {} 类", clz);
            return null;
        }

        try {
            return context.getBean(clz);
        } catch (NoSuchBeanDefinitionException e) {
            log.warn("No such bean of class {}.", clz);

            return null;
        }
    }
}
