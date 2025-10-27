package com.ajaxjs.security.iplist;

import com.ajaxjs.security.InterceptorAction;
import com.ajaxjs.util.ObjectHelper;
import com.ajaxjs.util.WebUtils;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Simply check the IP
 */
@Data
@Component
@EqualsAndHashCode(callSuper = true)
@ConditionalOnProperty(name = "security.IpList.enabled", havingValue = "true")
@ConfigurationProperties(prefix = "security.ip-list")
public class IpList extends InterceptorAction<IpListCheck> {
    /**
     * The ip allows to access
     */
    private List<String> whiteList;

    /**
     * The ip disallows to access
     */
    private List<String> blackList;

    @Override
    public boolean action(IpListCheck annotation, HttpServletRequest req) {
        String ip = WebUtils.getClientIp(req);

        if (!ObjectHelper.isEmpty(whiteList) && StringUtils.hasText(ip) && !whiteList.contains(ip))
            throw new SecurityException("Invalid IP:[" + ip + "] access");

        if (!ObjectHelper.isEmpty(blackList) && StringUtils.hasText(ip) && !blackList.contains(ip))
            throw new SecurityException("Invalid IP:[" + ip + "] access");

        return true;
    }
}
