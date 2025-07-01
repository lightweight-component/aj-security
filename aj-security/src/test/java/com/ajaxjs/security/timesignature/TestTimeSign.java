package com.ajaxjs.security.timesignature;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TestTimeSign {
    TimeSignature timeSignature = new TimeSignature();

    @Test
    public void testGenerateSignature() {
        timeSignature.setSecretKey("der3@x7Az#2");
        String signature = timeSignature.generateSignature();
        assertNotNull(signature);
        System.out.println(signature);

        assertTrue(timeSignature.verifySignature(signature));
    }

    @Test
    public void testErrorSignature() {
        assertThrows(SecurityException.class, () -> timeSignature.verifySignature("A785A0ADA9949DAF6C410202CF1E0A1C"));
    }

    @Test
    public void testGenerateSignatureOvertime() {
        // 获取当前时间
        long currentTimeMillis = System.currentTimeMillis();
        // 计算半小时之前的时间
        long halfAnHourAgo = currentTimeMillis - (30 * 60 * 1000);

        String signature = timeSignature.generateSignature(halfAnHourAgo);
        System.out.println(signature);
        assertFalse(timeSignature.verifySignature(signature));
    }
}
