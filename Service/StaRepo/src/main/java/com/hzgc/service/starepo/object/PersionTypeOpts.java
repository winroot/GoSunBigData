package com.hzgc.service.starepo.object;

import java.io.Serializable;

public class PersionTypeOpts implements Serializable{

    /**
     * 对象类型Id
     */
    private String id;
    /**
     * 对象类型
     */
    private String name;
    /**
     * 创建者
     */
    private String creator;
    /**
     * 备注
     */
    private String remark;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    @Override
    public String toString() {
        return "PersionTypeOpts{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", creator='" + creator + '\'' +
                ", remark='" + remark + '\'' +
                '}';
    }
}
