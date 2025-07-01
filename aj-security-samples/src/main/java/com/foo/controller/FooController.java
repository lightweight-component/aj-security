package com.foo.controller;

import com.ajaxjs.security.iplist.IpListCheck;
import com.ajaxjs.security.referer.HttpRefererCheck;
import com.ajaxjs.security.timesignature.TimeSignatureVerify;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
