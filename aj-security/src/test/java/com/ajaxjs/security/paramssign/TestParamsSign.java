package com.ajaxjs.security.paramssign;

import com.ajaxjs.util.ObjectHelper;
import org.junit.jupiter.api.Test;

import java.util.Map;

public class TestParamsSign {
    @Test
    void test() {
        Map<String, Object> params = ObjectHelper.mapOf("foo", 2, "bar", "bar2", "ccc", true);
        String secret = "A123456";

        ParamsSign paramsSign = new ParamsSign();
        paramsSign.setSecretKey(secret);

        String sign = paramsSign.sign(params);

        System.out.println(sign);
    }

}
