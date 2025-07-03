package com.foo.service;

import com.foo.controller.FooController;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@Slf4j
public class FooService implements FooController {
    @Override
    public int test() {
        return 888;
    }

    @Override
    public int testHttpRefererCheck() {
        return 888;
    }

    @Override
    public int TimeSignatureVerify() {
        return 0;
    }

    @Override
    public int IpListCheck() {
        return 0;
    }

    @Override
    public int ParamsSignCheck(Map<String, Object> params) {
        System.out.println( params);
        return 8;
    }
}
