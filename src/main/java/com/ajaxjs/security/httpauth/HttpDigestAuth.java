package com.ajaxjs.security.httpauth;

import com.ajaxjs.security.InterceptorAction;
import com.ajaxjs.security.common.SpringUtils;
import com.ajaxjs.util.Base64Helper;
import com.ajaxjs.util.MessageDigestHelper;
import com.ajaxjs.util.StrUtil;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.http.HttpHeaders;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Simply check the HttpBasicAuth
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class HttpDigestAuth extends InterceptorAction<HttpBasicDigestCheck> {
    String username;

    String password;

    String REALM = "my-digest-realm";

    @Override
    public boolean action(HttpBasicDigestCheck annotation, HttpServletRequest req) {
        String authHeader = req.getHeader(HttpHeaders.AUTHORIZATION);  // 获取 Referer 头

        if (StrUtil.isEmptyText(authHeader) || !authHeader.startsWith("Digest "))
            throw new SecurityException("Invalid HttpDigestAuth header.");

        // 解析 Authorization 头
        Map<String, String> authParams = parseDigestHeader(authHeader.substring(7));

        if (!username.equals(authParams.get("username"))) {
            sendDigestChallenge();
            return false;
        }

        String method = req.getMethod();
        String uri = authParams.get("uri");
        String nonce = authParams.get("nonce");
        String nc = authParams.get("nc");
        String cnonce = authParams.get("cnonce");
        String qop = authParams.get("qop");
        String responseDigest = authParams.get("response");

        // HA1 = MD5(username:realm:password)
        String ha1 = MessageDigestHelper.md5(username + ":" + REALM + ":" + password);

        // HA2 = MD5(method:uri)
        String ha2 = MessageDigestHelper.md5(method + ":" + uri);

        // response = MD5(HA1:nonce:nc:cnonce:qop:HA2)
        String validResponse = MessageDigestHelper.md5(ha1 + ":" + nonce + ":" + nc + ":" + cnonce + ":" + qop + ":" + ha2);

        if (validResponse.equals(responseDigest))
            return true;
        else {
            sendDigestChallenge();
            return false;
        }
    }

    private void sendDigestChallenge() {
        HttpServletResponse response = SpringUtils.getResponse();
        String nonce = Base64Helper.encode().input(UUID.randomUUID().toString()).getString();
        String header = String.format("Digest realm=\"%s\", qop=\"auth\", nonce=\"%s\", opaque=\"%s\"", REALM, nonce, MessageDigestHelper.md5(REALM));
        response.setHeader("WWW-Authenticate", header);
        response.setStatus(401);

        try {
            response.getWriter().write("Digest Auth Required");
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    private static Map<String, String> parseDigestHeader(String header) {
        Map<String, String> map = new HashMap<>();
        String[] parts = header.split(", ");

        for (String part : parts) {
            int eq = part.indexOf('=');

            if (eq > 0) {
                String key = part.substring(0, eq).trim();
                String value = part.substring(eq + 1).replaceAll("^\"|\"$", "");
                map.put(key, value);
            }
        }

        return map;
    }
}
