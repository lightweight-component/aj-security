package com.ajaxjs.security.classic;

import com.ajaxjs.util.CollUtils;
import com.ajaxjs.util.StrUtil;

import javax.servlet.Filter;
import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 安装安全 request、response
 */
@WebFilter("/*")
public class InstallFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        SecurityRequest securityRequest = new SecurityRequest(httpRequest);
        SecurityResponse securityResponse = new SecurityResponse((HttpServletResponse) response);

        Object obj = httpRequest.getServletContext().getAttribute("");

        if (obj != null) {

        }

        chain.doFilter(securityRequest, securityResponse);// 继续处理请求
    }

    @Override
    public void destroy() {
    }

    /**
     * 清理字符串中的特定模式。
     * 使用正则表达式匹配字符串中的模式，并根据提供的替换函数对匹配项进行处理。
     * 如果字符串为空或者没有匹配到任何模式，则原样返回字符串。
     * 正则匹配，若找到的话进行替换，或者按照 replFn 处理
     *
     * @param str    待清理的字符串
     * @param p      正则表达式模式，用于匹配字符串中的特定模式
     * @param replFn 替换函数，用于处理匹配到的模式。如果为 null，则简单地移除所有匹配的模式
     * @return 清理后的字符串。
     */
    private static String clean(String str, Pattern p, Function<Matcher, String> replFn) {
        if (StrUtil.isEmptyText(str))
            return str;

        Matcher matcher = p.matcher(str);

        if (matcher.find())
            return replFn == null ? matcher.replaceAll("") : replFn.apply(matcher);
        else
            return str;
    }

    private static final Pattern CRLF_Pattern = Pattern.compile("\\r|\\n");

    /**
     * 检测 CRLF 的过滤器 又叫做 HTTP Response Splitting
     *
     * @param str 待清理的字符串
     * @return 已清理的字符串
     */
    public static String cleanCRLF(String str) {
        return clean(str, CRLF_Pattern, null);
    }

    private static final Pattern XSS_Pattern = Pattern.compile("<script[^>]*?>.*?</script>");

    /**
     * 对于敏感字符串该如何处理
     */
    public enum Handle {
        /**
         * 转义字符串
         */
        TYPE_ESCAPE,

        /**
         * 删除字符串
         */
        TYPE_DELETE
    }

    /**
     * XSS 过滤器
     *
     * @param str 待清理的字符串
     * @return 已清理的字符串
     */
    public static String cleanXSS(String str) {
        return cleanXSS(str, Handle.TYPE_DELETE);
    }

    /**
     * XSS 过滤器
     *
     * @param str  待清理的字符串
     * @param type 处理类型
     * @return 已清理的字符串
     */
    public static String cleanXSS(String str, Handle type) {
        return clean(str, XSS_Pattern, matcher -> {
            if (type == Handle.TYPE_DELETE)
                return matcher.replaceAll("");
            else
                return matcher.group().replace("<", "&lt;").replace(">", "&gt;");
        });
    }

    /**
     * 是否在列表中
     *
     * @param str
     * @param list
     * @return true 表示为包含；false 表示为不包含
     */
    private static boolean isInList(String str, List<String> list) {
        if (CollUtils.isEmpty(list))
            return false;

        for (String pattern : list) {
            if (Pattern.matches(pattern, str)) // TODO 可以改为静态的 Pattern 编译类型，更快（但也更占内存）
                return true;
        }

        return false;
    }
}
