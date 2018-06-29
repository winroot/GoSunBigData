package com.hzgc.service.util.auth.scan;

import com.hzgc.service.util.auth.service.ResourceProviderSyncService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;

import java.util.Set;
import java.util.stream.Collectors;

public class ResourceProviderAutoSyncRunner implements ApplicationRunner {

    private ResourceProviderSyncService resourceProviderSyncService;

    private ResourceProviderAnnotationScanner resourceProviderAnnotationScanner;

    @Override
    public void run(ApplicationArguments applicationArguments) throws Exception {
        Set<ResourceProviderDefination> resourceProviderDefinations = resourceProviderAnnotationScanner.scan();
        if (resourceProviderDefinations != null && !resourceProviderDefinations.isEmpty()) {
            resourceProviderSyncService.sync(resourceProviderDefinations);
        }
    }

    public ResourceProviderSyncService getResourceProviderSyncService() {
        return resourceProviderSyncService;
    }

    public void setResourceProviderSyncService(ResourceProviderSyncService resourceProviderSyncService) {
        this.resourceProviderSyncService = resourceProviderSyncService;
    }

    public ResourceProviderAnnotationScanner getResourceProviderAnnotationScanner() {
        return resourceProviderAnnotationScanner;
    }

    public void setResourceProviderAnnotationScanner(ResourceProviderAnnotationScanner resourceProviderAnnotationScanner) {
        this.resourceProviderAnnotationScanner = resourceProviderAnnotationScanner;
    }
}
