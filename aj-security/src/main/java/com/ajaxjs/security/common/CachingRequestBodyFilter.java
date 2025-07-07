package com.ajaxjs.security.common;

import org.springframework.web.util.ContentCachingRequestWrapper;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

//@Component
//@Order(1)
public class CachingRequestBodyFilter implements Filter {
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        ServletRequest requestWrapper = new ContentCachingRequestWrapper((HttpServletRequest) request);
        chain.doFilter(requestWrapper, response);
    }
}