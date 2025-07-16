package com.ajaxjs.security.captcha.image;

import com.ajaxjs.security.InterceptorAction;
import com.ajaxjs.util.RandomTools;
import com.ajaxjs.util.StrUtil;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UncheckedIOException;

@Data
@Slf4j
@Component
@EqualsAndHashCode(callSuper = true)
@ConditionalOnProperty(name = "security.ImageCaptcha.enabled", havingValue = "true")
@ConfigurationProperties(prefix = "security.image-captcha")
public class ImageCaptcha extends InterceptorAction<ImageCaptchaCheck> {
    @Override
    public boolean action(ImageCaptchaCheck annotation, HttpServletRequest req) {
        String uuid = req.getParameter("uuid");

        if (StrUtil.isEmptyText(uuid))
            throw new IllegalArgumentException("The parameter 'uuid' is required.");

        String captchaCode = req.getParameter(CODE_KEY);

        if (StrUtil.isEmptyText(captchaCode))
            throw new IllegalArgumentException("The parameter '" + CODE_KEY + "' is required.");

        String key = KEY_PREFIX + uuid;
        String captchaCodeInRam = config.getCaptchaCodeFromRam().apply(key);

        if (StrUtil.isEmptyText(captchaCodeInRam))
            throw new SecurityException("The captcha code doesn't exist or expired. Please try to generate it again.");

        captchaCode = captchaCode.trim();

        if (!captchaCode.equalsIgnoreCase(captchaCodeInRam)) {
            log.info("user input:{}, ram:{}", captchaCode, captchaCodeInRam);
            throw new SecurityException("The captcha code is incorrect.");
        }

        // 验证码验证成功后，删除验证码
        config.getRemoveByKey().accept(key);

        return true;
    }

    @Autowired
    ImageCaptchaConfig config;

    private final static String KEY_PREFIX = "captcha_";

    private final static String CODE_KEY = "captcha_code";

    /**
     * 验证码有效时间
     */
    int expireSeconds;

    public void captchaImage(HttpServletRequest req, HttpServletResponse resp) {
        String uuid = req.getParameter("uuid");

        if (StrUtil.isEmptyText(uuid))
            throw new IllegalArgumentException("The parameter 'uuid' is required.");

        String capText = RandomTools.generateRandomString(5);// create the text for the image

        // store the text in the session
        log.info("store the text {} in the session {}", capText, uuid);
        config.getSaveToRam().save(KEY_PREFIX + uuid, capText, expireSeconds);

        resp.setDateHeader("Expires", 0);
        resp.setHeader("Cache-Control", "no-store, no-cache, must-revalidate");
        resp.addHeader("Cache-Control", "post-check=0, pre-check=0");
        resp.setHeader("Pragma", "no-cache");
        resp.setContentType("image/jpeg");

        try {
            ImageIO.write(config.getCaptchaImageProvider().getRenderedImage(70, 30, capText), "jpg", resp.getOutputStream());
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
}
