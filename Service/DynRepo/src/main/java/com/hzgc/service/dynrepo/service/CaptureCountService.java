package com.hzgc.service.dynrepo.service;

import com.hzgc.common.util.searchtype.SearchType;
import com.hzgc.service.dynrepo.attribute.AttributeCount;
import com.hzgc.service.dynrepo.object.CaptureCount;

import java.util.List;
import java.util.Map;

/**
 * 这个接口是为了大数据可视化而指定的，主要包含三个方法：
 * 1、dynaicNumberService：查询es的动态库，返回总抓拍数量和今日抓拍数量
 * 2、staticNumberService：查询es的静态库，返回每个平台下（对应platformId），每个对象库（对应pkey）下的人员的数量
 * 3、timeSoltNumber：根据入参ipcid的list、startTime和endTime去es查询到相应的值
 */
public interface CaptureCountService {

    String totolNum = "totolNumber";
    String todyTotolNumber = "todyTotolNumber";

    /**
     * 抓拍统计与今日抓拍统计
     * 查询es的动态库，返回总抓拍数量和今日抓拍数量
     *
     * @param ipcId 设备ID：ipcId
     * @return 返回总抓拍数和今日抓拍数量
     */
    Map<String, Integer> dynaicNumberService(List<String> ipcId);

    /**
     * 单平台下对象库人员统计
     * 查询es的静态库，返回每个平台下（对应platformId），每个对象库（对应pkey）下的人员的数量
     *
     * @param platformId 平台ID
     * @return 返回对应平台下的每个pkey的数量
     */
    Map<String, Integer> staticNumberService(String platformId);


    /**
     * 多设备每小时抓拍统计
     * 根据入参ipcid的list、startTime和endTime去es查询到相应的值
     *
     * @param ipcIds    设备ID
     * @param startTime 搜索的开始时间
     * @param endTime   搜索的结束时间
     * @return 返回对应这些设备ID的这段时间内每一小时抓拍的总数量
     */
    Map<String, Integer> timeSoltNumber(List<String> ipcIds, String startTime, String endTime);

    /**
     * 单设备抓拍统计（马燊偲）
     * 查询指定时间段内，指定设备抓拍的图片数量、该设备最后一次抓拍时间
     *
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @param ipcId     设备ID
     * @return CaptureCount 查询结果对象。对象内封装了：该时间段内该设备抓拍张数，该时间段内该设备最后一次抓拍时间。
     */
    CaptureCount captureCountQuery(String startTime, String endTime, String ipcId);

    /**
     * 多设备抓拍统计（陈柯）
     * 查询指定时间段内，指定的多个设备抓拍的图片数量
     *
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @param ipcId     多个设备id
     * @return 图片数量以long值表示
     */
    Long getCaptureCount(String startTime, String endTime, List<String> ipcId);

    /**
     * 抓拍属性统计 (刘思阳)
     * 查询指定时间段内，单个或某组设备中某种属性在抓拍图片中的数量
     *
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @param ipcIdList 单个或某组设备ID
     * @param type      统计类型
     * @return 单个或某组设备中某种属性在抓拍图片中的数量
     */
    List<AttributeCount> captureAttributeQuery(String startTime, String endTime, List<String> ipcIdList, SearchType type);
}
