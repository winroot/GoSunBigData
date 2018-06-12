package com.hzgc.service.util.auth.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author liuzhikun
 * @date 2018/04/27
 */
@ConfigurationProperties(prefix = AuthSyncProperties.AUTH_SYNC)
public class AuthSyncProperties {
    final static String AUTH_SYNC = "isap.auth.sync";

    private String basePackages;
    private String authSyncURL = "http://auth-center/permission/sync";
    private String resourceSyncURL = "http://auth-center/resource/sync";

    public static String getAuthSync() {
        return AUTH_SYNC;
    }

    public String getAuthSyncURL() {
        return authSyncURL;
    }

    public String getResourceSyncURL() {
        return resourceSyncURL;
    }

    public void setAuthSyncURL(String authSyncURL) {
        this.authSyncURL = authSyncURL;
    }

    public String getBasePackages() {
        return basePackages;
    }

    public void setBasePackages(String basePackages) {
        this.basePackages = basePackages;
    }
}
