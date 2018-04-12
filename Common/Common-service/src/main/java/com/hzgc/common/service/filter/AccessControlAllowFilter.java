package com.hzgc.common.service.filter;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import java.io.IOException;
import java.util.ResourceBundle;

/**
 * 允许跨域请求 filter
 * 已知冲突：
 * TokenValidateFilter 中直接返回 HttpResponse 无法进行拦截 , 返回 HttpResponse 时需要加入允许跨域访问的头信息
 * 需要的配置：
 * 需要在 isap-synthesissrv 下 resources/spring/spring-content.xml rest 协议的 extension 中加入。
 *
 * @author loucy
 * @date 2017/11/29
 */
public class AccessControlAllowFilter implements ContainerResponseFilter {
    public static final String ALLOW_ORIGIN ;
    static {
        ResourceBundle resourceBundle = ResourceBundle.getBundle("application");
        ALLOW_ORIGIN = resourceBundle.getString("access.allow.origin");
    }

    @Override
    public void filter(ContainerRequestContext containerRequestContext, ContainerResponseContext containerResponseContext) throws IOException {
        if (!ALLOW_ORIGIN.isEmpty()) {
            containerResponseContext.getHeaders().putSingle("Access-Control-Allow-Origin", ALLOW_ORIGIN);
        }
        containerResponseContext.getHeaders().putSingle("Access-Control-Allow-Headers", "Content-Type,x-requested-with,Authorization,Access-Control-Allow-Origin");
        containerResponseContext.getHeaders().putSingle("Access-Control-Allow-Methods", "POST,GET,PUT,DELETE,OPTIONS");
        containerResponseContext.getHeaders().putSingle("Access-Control-Max-Age", "360");
    }
}
