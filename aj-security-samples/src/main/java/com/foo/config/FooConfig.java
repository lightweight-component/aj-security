package com.foo.config;

import org.aopalliance.intercept.MethodInterceptor;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.aop.support.NameMatchMethodPointcut;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class FooConfig implements WebMvcConfigurer {
    // 必须有这个Bean，Spring才能自动为Advisor创建代理
    @Bean
    public DefaultAdvisorAutoProxyCreator autoProxyCreator() {
        return new DefaultAdvisorAutoProxyCreator();
    }

    // 定义切面逻辑
    @Bean
    public DefaultPointcutAdvisor paramsSignCheckAdvisor() {
        NameMatchMethodPointcut pointcut = new NameMatchMethodPointcut();
        pointcut.setMappedName("ParamsSignCheck"); // 精确匹配方法名
        MethodInterceptor advice = invocation -> {
            // 前置逻辑
            System.out.println("Before ParamsSignCheck, params = " + java.util.Arrays.toString(invocation.getArguments()));
            if (false)
                return -1;

            // 执行原方法
            return invocation.proceed();
        };

        return new DefaultPointcutAdvisor(pointcut, advice);
    }
}
