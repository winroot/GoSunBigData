package com.hzgc.service.starepo.bean;

import com.hzgc.common.service.table.column.ObjectInfoTable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Map;
import java.util.UUID;

/**
 * 静态库人员中每个人的信息
 */
@ApiModel(value = "静态库人员中每个人的信息")
@Data
public class PersonObject implements Serializable{

    /**
     * 数据库中的唯一标志
     */
    @ApiModelProperty(value = "数据库中的唯一标志")
    private String id;

    /**
     * 对象类型key
     */
    @ApiModelProperty(value = "对象类型key")
    private String pkey;

    /**
     * 平台Id
     */
    @ApiModelProperty(value = "平台ID")
    private String platformid;

    /**
     * 姓名
     */
    @ApiModelProperty(value = "姓名")
    private String name;

    /**
     * 性别
     */
    @ApiModelProperty(value = "性别")
    private int sex;

    /**
     * 身份证号
     */
    @ApiModelProperty(value = "身份证号")
    private String idcard;

    /**
     * 照片
     */
    @ApiModelProperty(value = "照片")
    private byte[] photo;

    /**
     * 特征值
     */
    @ApiModelProperty(value = "特征值")
    private float[] feature;

    /**
     * 创建者
     */
    @ApiModelProperty(value = "创建者")
    private String creator;

    /**
     * 创建者手机号
     */
    @ApiModelProperty(value = "创建者手机号")
    private String cphone;

    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    private Timestamp createtime;

    /**
     * 更新时间
     */
    @ApiModelProperty(value = "更新时间")
    private Timestamp updatetime;

    /**
     * 布控理由
     */
    @ApiModelProperty(value = "不空理由")
    private String reason;

    /**
     * 人车标志
     */
    @ApiModelProperty(value = "人车标志")
    private String tag;

    /**
     * 0,重点关注，1，非重点关注
     */
    @ApiModelProperty(value = "0,重点关注，1，非重点关注")
    private int important;

    /**
     * 0,常住人口，1，建议迁出
     */
    @ApiModelProperty(value = "0,常住人口，1，建议迁出")
    private int status;

    /**
     * 相似度
     */
    @ApiModelProperty(value = "相似度")
    private float sim;

    /**
     * 地址
     */
    @ApiModelProperty(value = "地址")
    private String location;

    public static PersonObject mapToPersonObject(Map<String, Object> person) {
        PersonObject personObject = new PersonObject();

        String id = UUID.randomUUID().toString().replace("-", "");
        personObject.setId(id);
        personObject.setPkey((String) person.get(ObjectInfoTable.PKEY));
        personObject.setPlatformid((String) person.get(ObjectInfoTable.PLATFORMID));
        personObject.setName((String) person.get(ObjectInfoTable.NAME));
        if (person.get(ObjectInfoTable.SEX) != null) {
            personObject.setSex(Integer.parseInt((String) person.get(ObjectInfoTable.SEX)));
        }
        personObject.setIdcard((String) person.get(ObjectInfoTable.IDCARD));
        personObject.setPhoto((byte[]) person.get(ObjectInfoTable.PHOTO));
        personObject.setFeature((float[]) person.get(ObjectInfoTable.FEATURE));
        personObject.setCreator((String) person.get(ObjectInfoTable.CREATOR));
        personObject.setCphone((String) person.get(ObjectInfoTable.CPHONE));
        personObject.setReason((String) person.get(ObjectInfoTable.REASON));
        personObject.setTag((String) person.get(ObjectInfoTable.TAG));
        if (person.get(ObjectInfoTable.IMPORTANT) != null) {
            personObject.setImportant(Integer.parseInt((String) person.get(ObjectInfoTable.IMPORTANT)));
        }
        if (person.get(ObjectInfoTable.STATUS) != null) {
            personObject.setStatus(Integer.parseInt((String) person.get(ObjectInfoTable.STATUS)));
        }
        long dateNow = System.currentTimeMillis();
        personObject.setUpdatetime(new Timestamp(dateNow));
        return personObject;
    }

    public static Object[] otherArrayToObject(float [] in) {
        if (in == null || in.length <= 0) {
            return null;
        }
        Object[] out = new Object[in.length];
        for (int i = 0; i < in.length; i++) {
            out[i] = in[i];
        }
        return out;
    }

    public static Object[] otherArrayToObject(byte [] in) {
        if (in == null || in.length <= 0) {
            return null;
        }
        Object[] out = new Object[in.length];
        for (int i = 0; i < in.length; i++) {
            out[i] = in[i];
        }
        return out;
    }
}
