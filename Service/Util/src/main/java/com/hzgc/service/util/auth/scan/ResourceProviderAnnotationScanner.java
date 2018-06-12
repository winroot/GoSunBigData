package com.hzgc.service.util.auth.scan;

import com.hzgc.service.util.auth.annotation.ResourceProvider;
import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.scanners.TypeAnnotationsScanner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;


/**
 * 资源提供者注解扫描器
 */
public class ResourceProviderAnnotationScanner {
    private static Logger logger = LoggerFactory.getLogger(ResourceProviderAnnotationScanner.class);

    private List<String> basePackages;

    private Set<ResourceProviderDefination> resourceProviderDefinationSet = new LinkedHashSet<>();

    public List<String> getBasePackages() {
        return basePackages;
    }

    public void setBasePackages(List<String> basePackages) {
        this.basePackages = basePackages;
    }

    public Set<ResourceProviderDefination> scan() {
        if (CollectionUtils.isEmpty(basePackages)) {
            return null;
        }
        Reflections reflections = new Reflections(StringUtils.toStringArray(basePackages), new TypeAnnotationsScanner(), new SubTypesScanner());

        Set<Class<?>> resourceProviderSet = reflections.getTypesAnnotatedWith(ResourceProvider.class);
        if (resourceProviderSet == null || resourceProviderSet.isEmpty()) {
            return null;
        }
        resourceProviderSet.forEach(f -> {
            ResourceProviderDefination resourceProviderDefination = parseResourceProvider(f);
            resourceProviderDefinationSet.add(resourceProviderDefination);
        });
        return resourceProviderDefinationSet;
    }

    private ResourceProviderDefination parseResourceProvider(Class<?> resource) {
        try {
            ResourceProviderDefination resourceProviderDefination = new ResourceProviderDefination();
            Map<String, Object> providerInfo = AnnotationUtils.getAnnotationAttributes(resource.getAnnotation(ResourceProvider.class));
            if (null == providerInfo || providerInfo.isEmpty()) {
                throw new IllegalArgumentException("资源提供者参数配置错误");
            }
            for (Map.Entry<String, Object> entry : providerInfo.entrySet()) {
                if ("uri".equals(entry.getKey())) {
                    resourceProviderDefination.setResourceUri((String) entry.getValue());
                } else if ("type".equals(entry.getKey())) {
                    resourceProviderDefination.setResourceType((String) entry.getValue());
                }
            }
            return resourceProviderDefination;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
