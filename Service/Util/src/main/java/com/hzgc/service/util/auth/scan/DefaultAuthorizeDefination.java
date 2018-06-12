package com.hzgc.service.util.auth.scan;

import java.util.Objects;

/**
 * @author liuzhikun
 * @date 2018/04/27
 */
public class DefaultAuthorizeDefination {
    private String name;
    private String menu;
    private String permission;
    private String description;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMenu() {
        return menu;
    }

    public void setMenu(String menu) {
        this.menu = menu;
    }

    public String getPermission() {
        return permission;
    }

    public void setPermission(String permission) {
        this.permission = permission;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "DefaultAuthorizeDefination{" +
                "name='" + name + '\'' +
                ", menu='" + menu + '\'' +
                ", permission='" + permission + '\'' +
                ", description='" + description + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        DefaultAuthorizeDefination that = (DefaultAuthorizeDefination) o;
        return Objects.equals(permission, that.permission);
    }

    @Override
    public int hashCode() {

        return Objects.hash(permission);
    }
}
