package com.foo.controller;

import com.ajaxjs.security.desensitize.annotation.Desensitize;
import com.ajaxjs.security.iplist.IpListCheck;
import com.ajaxjs.security.paramssign.ParamsSignCheck;
import com.ajaxjs.security.referer.HttpRefererCheck;
import com.ajaxjs.security.timesignature.TimeSignatureVerify;
import org.springframework.web.bind.annotation.*;
import com.foo.model.User;
import java.util.Map;

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
    @ParamsSignCheck
    int ParamsSignCheck(@RequestBody Map<String, Object> params);

    @GetMapping("/encrypted_body")
    User getUser();

    @PostMapping("/encrypted_params")
    int encryptedParams(@RequestBody User user);
}
