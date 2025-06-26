package com.ajaxjs.security.httpauth;

import com.ajaxjs.security.InterceptorAction;
import com.ajaxjs.util.Base64Helper;
import com.ajaxjs.util.StrUtil;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.http.HttpHeaders;

import javax.servlet.http.HttpServletRequest;

/**
 * Simply check the HttpBasicAuth
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class HttpBasicAuth extends InterceptorAction<HttpBasicAuthCheck> {
    String username;

    String password;

    @Override
    public boolean action(HttpBasicAuthCheck annotation, HttpServletRequest req) {
        String authHeader = req.getHeader(HttpHeaders.AUTHORIZATION);  // 获取 Referer 头

        if (StrUtil.isEmptyText(authHeader) || !authHeader.startsWith("Basic "))
            throw new SecurityException("Invalid HttpBasicAuth header.");

        String base64Credentials = authHeader.substring("Basic ".length());
        String credentials = Base64Helper.decode().input(base64Credentials).getString();
        String[] values = credentials.split(":", 2);
        String _username = values[0];
        String _password = values[1];

        return username.equals(_username) && password.equals(_password);
    }

    public static String create(String username, String password) {
        return "Basic " + Base64Helper.encode().input(username + ":" + password).getString();
    }
}
