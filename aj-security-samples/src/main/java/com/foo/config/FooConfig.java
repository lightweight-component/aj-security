package com.foo.config;

import com.ajaxjs.security.captcha.image.ImageCaptchaConfig;
import com.ajaxjs.security.captcha.image.impl.SimpleCaptchaImage;
import com.ajaxjs.security.encryptedbody.EncryptedBodyConverter;
import com.foo.common.ResponseResultWrapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
public class FooConfig implements WebMvcConfigurer {
    // 必须有这个Bean，Spring才能自动为Advisor创建代理
//    @Bean
//    public DefaultAdvisorAutoProxyCreator autoProxyCreator() {
//        return new DefaultAdvisorAutoProxyCreator();
//    }
//
//    // 定义切面逻辑
//    @Bean
//    public DefaultPointcutAdvisor paramsSignCheckAdvisor() {
//        NameMatchMethodPointcut pointcut = new NameMatchMethodPointcut();
//        pointcut.setMappedName("ParamsSignCheck"); // 精确匹配方法名
//        MethodInterceptor advice = invocation -> {
//            // 前置逻辑
//            System.out.println("Before ParamsSignCheck, params = " + java.util.Arrays.toString(invocation.getArguments()));
//            if (false)
//                return -1;
//
//            // 执行原方法
//            return invocation.proceed();
//        };
//
//        return new DefaultPointcutAdvisor(pointcut, advice);
//    }

    @Value("${security.EncryptedBody.publicKey}")
    private String apiPublicKey;

    @Value("${security.EncryptedBody.privateKey}")
    private String apiPrivateKey;

    @Value("${security.EncryptedBody.enabled}")
    private boolean isEnabled;

    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        EncryptedBodyConverter<ResponseResultWrapper> converter = new EncryptedBodyConverter<>(apiPublicKey, apiPrivateKey, ResponseResultWrapper.class);
        converter.setEnabled(isEnabled);

        converters.add(0, converter);
    }

    static final SimpleCache RAM = new SimpleCache(); // JVM 缓存

    @Bean
    ImageCaptchaConfig ImageCaptchaConfig() {
        ImageCaptchaConfig config = new ImageCaptchaConfig();
        config.setCaptchaImageProvider(new SimpleCaptchaImage());
        config.setSaveToRam(RAM::add);
        config.setCaptchaCodeFromRam(key -> {
            SimpleCache.Item item = RAM.get(key);
            return item == null ? null : item.getValue();
        });
        config.setRemoveByKey(RAM::remove);

        return config;
    }
}
