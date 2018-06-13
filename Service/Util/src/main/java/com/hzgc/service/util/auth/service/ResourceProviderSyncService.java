package com.hzgc.service.util.auth.service;

import com.hzgc.service.util.auth.scan.ResourceProviderDefination;

import java.util.Set;

/**
 * 资源提供者注册
 *
 * @author wangdl
 */
public interface ResourceProviderSyncService {
    void sync(Set<ResourceProviderDefination> resourceProviderSyncServices);
}
