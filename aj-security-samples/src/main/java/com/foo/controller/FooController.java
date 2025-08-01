package com.foo.controller;

import com.ajaxjs.security.SecurityInterceptor;
import com.ajaxjs.security.captcha.cloudflare.CloudflareCheck;
import com.ajaxjs.security.captcha.google.GoogleCaptcha;
import com.ajaxjs.security.captcha.google.GoogleCaptchaCheck;
import com.ajaxjs.security.captcha.image.ImageCaptcha;
import com.ajaxjs.security.captcha.image.ImageCaptchaCheck;
import com.ajaxjs.security.desensitize.annotation.Desensitize;
import com.ajaxjs.security.iplist.IpListCheck;
import com.ajaxjs.security.limit.simplelimit.SimpleLimitCheck;
import com.ajaxjs.security.paramssign.ParamsSignCheck;
import com.ajaxjs.security.referer.HttpRefererCheck;
import com.ajaxjs.security.timesignature.TimeSignatureVerify;
import com.foo.model.User;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/foo")
public interface FooController {
    @GetMapping
    int test();

    @GetMapping("/HttpRefererCheck")
    @HttpRefererCheck
    int testHttpRefererCheck();

    @GetMapping("/TimeSignatureVerify")
    @TimeSignatureVerify
    int TimeSignatureVerify();

    @GetMapping("/IpListCheck")
    @IpListCheck
    int IpListCheck();

    @GetMapping("/user_desensitize")
    @Desensitize
    User UserDesensitize();

    @PostMapping("/ParamsSignCheck")
//    int ParamsSignCheck( @ParamsSignCheck @RequestBody Map<String, Object> params);
    int ParamsSignCheck(@ParamsSignCheck User user);

    @GetMapping("/encrypted_body")
    User getUser();

    @PostMapping("/encrypted_params")
    int encryptedParams(@RequestBody User user);

    @PostMapping("/captcha_cloudflare")
    @CloudflareCheck
    boolean cloudflare(@RequestParam("cf-turnstile-response") String token, @ModelAttribute User user);

    @PostMapping("/captcha_google")
    @GoogleCaptchaCheck
    boolean google(@RequestParam(GoogleCaptcha.PARAM_NAME) String token, @ModelAttribute User user);

    @GetMapping("/captcha")
    default void showCaptcha(HttpServletRequest req, HttpServletResponse response) {
        ImageCaptcha imageCaptcha = SecurityInterceptor.getBean(ImageCaptcha.class);
        imageCaptcha.captchaImage(req, response);
    }

    @PostMapping("/create_user")
    @ImageCaptchaCheck
    boolean createUser(@ModelAttribute User user);

    @GetMapping("/simple_limit")
    @SimpleLimitCheck
    boolean simpleLimit();

    @GetMapping("/trace_id")
    boolean traceId();


}
