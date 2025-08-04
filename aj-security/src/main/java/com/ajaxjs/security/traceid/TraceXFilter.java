package com.ajaxjs.security.traceid;

import com.ajaxjs.util.BoxLogger;
import com.ajaxjs.util.StrUtil;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.util.ContentCachingRequestWrapper;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

/**
 * Adds TraceId on every single Request, and adds ContentCachingRequestWrapper/ContentCachingResponseWrapper
 */
@WebFilter("/**")
@Component
public class TraceXFilter implements Filter {
    private final static String X_TRACE = "x-trace";

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        String traceId = req.getHeader(X_TRACE);

        if (!StringUtils.hasLength(traceId))
            traceId = UUID.randomUUID().toString().replace("-", StrUtil.EMPTY_STRING).toUpperCase();

        MDC.put(BoxLogger.TRACE_KEY, traceId);

        // 包装请求，缓存 body
        ContentCachingRequestWrapper wrappedRequest = new ContentCachingRequestWrapper(req);
//        ContentCachingResponseWrapper wrappedResponse = new ContentCachingResponseWrapper((HttpServletResponse) response);

        chain.doFilter(wrappedRequest, response);
    }

    public static String getRequestBody(HttpServletRequest request) {
        if (request instanceof ContentCachingRequestWrapper) {
            ContentCachingRequestWrapper wrapper = (ContentCachingRequestWrapper) request;

            return new String(wrapper.getContentAsByteArray(), StandardCharsets.UTF_8);
        }

        return "";
    }
}