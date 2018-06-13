package com.hzgc.service.util.auth.service;

import com.hzgc.service.util.auth.scan.DefaultAuthorizeDefination;

import java.util.Set;

/**
 * 权限颗粒同步服务
 *
 * @author liuzhikun
 */
public interface AuthorizeSyncService {
    void sync(Set<DefaultAuthorizeDefination> authorizeDefinations);
}
