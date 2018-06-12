package com.hzgc.service.util.auth.scan;

import java.util.Objects;

/**
 * 定义资源提供者
 *
 * @author wangdl
 */
public class ResourceProviderDefination {
    private String name;
    private String resourceUri;
    private String resourceType;

    public String getResourceType() {
        return resourceType;
    }

    public void setResourceType(String resourceType) {
        this.resourceType = resourceType;
    }

    @Override
    public String toString() {
        return "ResourceProviderDefination{" +
                "applicationName='" + name + '\'' +
                ", resourceUrl='" + resourceUri + '\'' +
                ", resourceType='" + resourceType + '\'' +
                "}";
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ResourceProviderDefination that = (ResourceProviderDefination) o;
        return Objects.equals(resourceUri, that.resourceUri);
    }

    @Override
    public int hashCode() {

        return Objects.hash(resourceUri);
    }

    public String getResourceUri() {
        return resourceUri;
    }

    public void setResourceUri(String resourceUri) {
        this.resourceUri = resourceUri;
    }
}
