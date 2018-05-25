package com.hzgc.service.dynrepo.bean.platform;

import lombok.Data;

@Data
public class DeviceDTO {
    private String id;
    private Long platId;
    private String code;
    private String name;
    private Integer devType;
    private Integer extType;
    private Integer channelType;
    private Integer channelSubType;
    private Byte status;
    private String parent;
    private String departmentId;
    private String description;
    private String serial;
}
