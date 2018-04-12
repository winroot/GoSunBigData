package com.hzgc.common.service;

import java.util.List;

/**
 * 批量id类型
 *
 * @param <T>
 * @author liuzk
 */
public class IdsType<T> {
    private List<T> id;

    public List<T> getId() {
        return id;
    }

    public void setId(List<T> id) {
        this.id = id;
    }
}
