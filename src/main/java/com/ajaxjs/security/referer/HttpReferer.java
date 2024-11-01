package com.ajaxjs.security.referer;

import com.ajaxjs.security.SecurityFilter;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Simply check the HTTP Referer
 */
public class HttpReferer implements SecurityFilter {
    protected List<String> ALLOWED_REFERRERS;

    @Override
    public void onRequest(HttpServletRequest req) {
        // 获取 Referer 头
        String referer = req.getHeader("Referer");

        if (!StringUtils.hasText(referer) || !ALLOWED_REFERRERS.contains(referer))
            throw new HttpRefererException("Invalid Referer header.");
    }
}
