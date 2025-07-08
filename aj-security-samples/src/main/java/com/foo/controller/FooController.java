package com.foo.controller;

import com.ajaxjs.framework.database.IgnoreDataBaseConnect;
import com.ajaxjs.security.desensitize.annotation.Desensitize;
import com.ajaxjs.security.iplist.IpListCheck;
import com.ajaxjs.security.paramssign.ParamsSignCheck;
import com.ajaxjs.security.referer.HttpRefererCheck;
import com.ajaxjs.security.timesignature.TimeSignatureVerify;
import com.foo.model.User;
import org.springframework.web.bind.annotation.*;

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
}
