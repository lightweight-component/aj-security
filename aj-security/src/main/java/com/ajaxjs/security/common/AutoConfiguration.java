package com.ajaxjs.security.common;

import com.ajaxjs.security.SecurityInterceptor;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@ComponentScan(basePackages = "com.ajaxjs.security")
public class AutoConfiguration implements WebMvcConfigurer {
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(initSecurityInterceptor());
    }

    @Bean
    HandlerInterceptor initSecurityInterceptor() {
        return new SecurityInterceptor();
    }

    @Bean
    public FilterRegistrationBean<CachingRequestBodyFilter> cachingRequestBodyFilter() {
        FilterRegistrationBean<CachingRequestBodyFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new CachingRequestBodyFilter());
        registrationBean.setOrder(1); // 数字越小越靠前，尽早执行
        registrationBean.addUrlPatterns("/*"); // 作用于所有路径
        return registrationBean;
    }
}
