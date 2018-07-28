package com.hzgc.service.starepo.bean.export;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * 静态库人员中每个人的信息
 */
@ApiModel(value = "静态库人员中每个人的信息")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PersonObject implements Serializable{

    /**
     * 数据库中的唯一标志
     */
    @ApiModelProperty(value = "数据库中的唯一标志")
    private String objectID;

    /**
     * 对象类型key
     */
    @ApiModelProperty(value = "对象类型key")
    private String objectTypeKey;

    /**
     * 对象类型名称
     */
    @ApiModelProperty(value = "对象类型名称")
    private String objectTypeName;

    /**
     * 对象名称
     */
    @ApiModelProperty(value = "对象名称")
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
     * 创建者
     */
    @ApiModelProperty(value = "创建者")
    private String creator;

    /**
     * 创建者手机号
     */
    @ApiModelProperty(value = "创建者手机号")
    private String creatorConractWay;

    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    private String createTime;

    /**
     * 布控理由
     */
    @ApiModelProperty(value = "布控理由")
    private String reason;

    /**
     * 0,重点关注，1，非重点关注
     */
    @ApiModelProperty(value = "0：重点关注，1：非重点关注")
    private int followLevel;

    /**
     * 相似度
     */
    @ApiModelProperty(value = "相似度")
    private float similarity;

    /**
     * 地址
     */
    @ApiModelProperty(value = "地址")
    private String location;

    public static PersonObject builder() {
        return new PersonObject();
    }

    public String getObjectTypeKey() {
        return objectTypeKey;
    }

    public PersonObject setObjectTypeKey(String objectTypeKey) {
        this.objectTypeKey = objectTypeKey;
        return this;
    }

    public String getObjectTypeName() {
        return objectTypeName;
    }

    public PersonObject setObjectTypeName(String objectTypeName) {
        this.objectTypeName = objectTypeName;
        return this;
    }

    public int getSex() {
        return sex;
    }

    public PersonObject setSex(int sex) {
        this.sex = sex;
        return this;
    }

    public String getIdcard() {
        return idcard;
    }

    public PersonObject setIdcard(String idcard) {
        this.idcard = idcard;
        return this;
    }

    public String getCreator() {
        return creator;
    }

    public PersonObject setCreator(String creator) {
        this.creator = creator;
        return this;
    }

    public String getCreatorConractWay() {
        return creatorConractWay;
    }

    public PersonObject setCreatorConractWay(String creatorConractWay) {
        this.creatorConractWay = creatorConractWay;
        return this;
    }

    public String getReason() {
        return reason;
    }

    public PersonObject setReason(String reason) {
        this.reason = reason;
        return this;
    }

    public float getSimilarity() {
        return similarity;
    }

    public PersonObject setSimilarity(float similarity) {
        this.similarity = similarity;
        return this;
    }

    public String getLocation() {
        return location;
    }

    public PersonObject setLocation(String location) {
        this.location = location;
        return this;
    }

    public String getObjectID() {
        return objectID;
    }

    public PersonObject setObjectID(String objectID) {
        this.objectID = objectID;
        return this;
    }

    public String getName() {
        return name;
    }

    public PersonObject setName(String name) {
        this.name = name;
        return this;
    }

    public String  getCreateTime() {
        return createTime;
    }

    public PersonObject setCreateTime(String createTime) {
        this.createTime = createTime;
        return this;
    }

    public int getFollowLevel() {
        return followLevel;
    }

    public PersonObject setFollowLevel(int followLevel) {
        this.followLevel = followLevel;
        return this;
    }
}
