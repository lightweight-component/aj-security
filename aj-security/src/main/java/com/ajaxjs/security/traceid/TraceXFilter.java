package com.ajaxjs.security.traceid;

import com.ajaxjs.util.BoxLogger;
import com.ajaxjs.util.StrUtil;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.UUID;

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

        chain.doFilter(request, response);
    }
}