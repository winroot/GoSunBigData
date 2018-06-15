package com.hzgc.service.visual.service;

import com.hzgc.common.util.empty.IsEmpty;
import com.hzgc.service.util.api.bean.DeviceDTO;
import com.hzgc.service.util.api.service.DeviceQueryService;
import com.hzgc.service.visual.bean.CaptureCountBean;
import com.hzgc.service.visual.bean.FaceDayStatistic;
import com.hzgc.service.visual.bean.StatisticsBean;
import com.hzgc.service.visual.bean.TimeSlotNumber;
import com.hzgc.service.visual.dao.ElasticSearchDao;
import com.hzgc.service.visual.dao.EsSearchParam;
import com.hzgc.service.visual.util.DateUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.util.Base64;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.bucket.terms.StringTerms;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 这个方法是为了大数据可视化而指定的，继承于CaptureCountService，主要包含三个方法：
 * 1、dynaicNumberService：查询es的动态库，返回总抓拍数量和今日抓拍数量
 * 2、staticNumberService：查询es的静态库，返回每个平台下（对应platformId），每个对象库（对应pkey）下的人员的数量
 * 3、timeSoltNumber：根据入参ipcid的list、startTime和endTime去es查询到相应的值
 */
@Service
@Slf4j
public class CaptureCountService {
    private static final int DAY_YMD_END = 10;
    private static final int HOUR_YMD_END = 13;
    private static final Integer OFFSET_DAY_SEVEN = 7;
    private static final Integer OFFSET_DAY_ONE = 1;
    private static final String FTP_NAME = "admin";
    private static final String FTP_PASSWORD = "123456";
    private CaptureCountBean saveCaptureCount = new CaptureCountBean(0, 0);

    private DeviceQueryService deviceQueryService;

    @SuppressWarnings("unused")
    private ElasticSearchDao elasticSearchDao;

    @Autowired
    public CaptureCountService(DeviceQueryService deviceQueryService, ElasticSearchDao elasticSearchDao) {
        this.deviceQueryService = deviceQueryService;
        this.elasticSearchDao = elasticSearchDao;
    }

    /**
     * 抓拍统计与今日抓拍统计
     * 查询es的动态库，返回总抓拍数量和今日抓拍数量
     *
     * @param deviceIdList 设备ID：deviceIdList 可不填
     * @return 返回总抓拍数量和今日抓拍数量
     */
    public CaptureCountBean dynamicNumberService(List<Long> deviceIdList) {
        //将deviceIdList转换为ipcIdList
        List<String> ipcIdList = new ArrayList<>();
        if (deviceIdList != null && deviceIdList.size() > 0) {
            Map<String, DeviceDTO> deviceDTOMap = deviceQueryService.getDeviceInfoByBatchId(deviceIdList);
            for (Map.Entry<String, DeviceDTO> entry : deviceDTOMap.entrySet()) {
                ipcIdList.add(entry.getValue().getSerial());
            }
        }

        SearchResponse[] responsesArray = elasticSearchDao.dynamicNumberService(ipcIdList);
        SearchResponse searchResponse0 = responsesArray[0];
        SearchHits searchHits0 = searchResponse0.getHits();
        int totalNumber = (int) searchHits0.getTotalHits();
        SearchResponse searchResponse1 = responsesArray[1];
        SearchHits searchHits1 = searchResponse1.getHits();
        int todaytotalNumber = (int) searchHits1.getTotalHits();
        CaptureCountBean bean = new CaptureCountBean(todaytotalNumber, totalNumber);
        saveCaptureCount.save(bean);
        return saveCaptureCount;
    }

    /**
     * 多设备每小时抓拍统计
     * 根据入参ipcid的list、startTime和endTime去es查询到相应的值
     *
     * @param deviceIdList 设备ID：deviceId
     * @param startTime    搜索的开始时间
     * @param endTime      搜索的结束时间
     * @return 返回某段时间内，这些ipcid的抓拍的总数量
     */
    public TimeSlotNumber timeSoltNumber(List<Long> deviceIdList, String startTime, String endTime) {
        TimeSlotNumber slotNumber = new TimeSlotNumber();
        Boolean flag = false;
        if (IsEmpty.strIsRight(startTime) && IsEmpty.strIsRight(endTime)) {
            // 整理成整点
            startTime = DateUtils.checkTime(startTime);
            endTime = DateUtils.checkTime(endTime);
        } else if (IsEmpty.strIsRight(startTime)) {
            // 只传了一个开始时间
            startTime = DateUtils.checkTime(startTime);
            endTime = DateUtils.getSpecifiedDayAfter(startTime, OFFSET_DAY_ONE);
        } else if (IsEmpty.strIsRight(endTime)) {
            // 只传了一个结束时间
            endTime = DateUtils.checkTime(endTime);
            startTime = DateUtils.getSpecifiedDayBefore(endTime, OFFSET_DAY_ONE);
        } else {
            // 时间没有传
            endTime = DateUtils.checkTime(DateUtils.formatDateTime(new Date()));
            startTime = DateUtils.getSpecifiedDayBefore(endTime, OFFSET_DAY_ONE);
            flag = true;
        }

        //将deviceIdList转换为ipcIdList
        List<String> ipcIdList = new ArrayList<>();
        if (deviceIdList.size() > 0) {
            Map<String, DeviceDTO> deviceDTOMap = deviceQueryService.getDeviceInfoByBatchId(deviceIdList);
            for (Map.Entry<String, DeviceDTO> entry : deviceDTOMap.entrySet()) {
                ipcIdList.add(entry.getValue().getSerial());
            }
        }

        List<String> times;
        times = getHourTime(startTime, endTime);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(EsSearchParam.TIMEFORMAT_YMDHMS);
        for (String oneHourStart : times) {
            String oneHourEnd = null;
            int count = 0;
            try {
                long ohs = simpleDateFormat.parse(oneHourStart).getTime();
                long ohe = ohs + EsSearchParam.LONG_OBNEHOUR;
                oneHourEnd = simpleDateFormat.format(ohe);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            SearchResponse response = elasticSearchDao.timeSoltNumber(ipcIdList, oneHourStart, oneHourEnd);
            Map<String, Aggregation> aggMap = response.getAggregations().asMap();
            for (String a : aggMap.keySet()) {
                StringTerms terms = (StringTerms) aggMap.get(a);
                for (StringTerms.Bucket bucket : terms.getBuckets()) {
                    count += bucket.getDocCount();
                }
            }
            slotNumber.getFaceList().add(new FaceDayStatistic(oneHourStart, count));
        }

        if (slotNumber.getFaceList().size() > 0) {
            slotNumber.getFaceList().sort((o1, o2) -> {
                if (o1.getId().compareTo(o2.getId()) > 0) {
                    return 1;
                }
                return -1;
            });
            for (FaceDayStatistic faceDayStatistic : slotNumber.getFaceList()) {
                faceDayStatistic.setDate(faceDayStatistic.getId().substring(0, DAY_YMD_END));
                faceDayStatistic.setId(faceDayStatistic.getId().substring(DAY_YMD_END + 1, HOUR_YMD_END));
            }
            // 时间段不传，去除第一个数据
            if (flag) {
                slotNumber.getFaceList().remove(0);
            }
        }
        return slotNumber;
    }

    /**
     * 抓拍统计
     *
     * @param startTime    开始时间
     * @param endTime      结束时间
     * @param deviceIdList 设备Id
     * @return 每天抓拍数
     */
    public List<StatisticsBean> getStatisticsFace(String startTime, String endTime, List<Long> deviceIdList) {
        List<StatisticsBean> statisticsBeanList = new ArrayList<>();
        if (endTime != null && endTime.matches("[0-9]{4}-[0-9]{1,2}-[0-9]{1,2}")) {
            endTime = endTime + " 00:00:00";
        }
        if (startTime != null && startTime.matches("[0-9]{4}-[0-9]{1,2}-[0-9]{1,2}")) {
            startTime = startTime + " 00:00:00";
        }

        // 时间段判断
        if (StringUtils.isBlank(startTime) && StringUtils.isBlank(endTime)) {
            endTime = DateUtils.formatDateTime(new Date());
            startTime = DateUtils.getSpecifiedDayBefore(endTime, OFFSET_DAY_SEVEN);
        } else if (StringUtils.isBlank(startTime)) {
            startTime = DateUtils.getSpecifiedDayBefore(endTime, OFFSET_DAY_SEVEN);
        } else if (StringUtils.isBlank(endTime)) {
            endTime = DateUtils.getSpecifiedDayAfter(startTime, OFFSET_DAY_SEVEN);
        }

        //调接口，将 deviceId 转换为IpcId
        List<String> ipcIdList = new ArrayList<>();
        Map<String, DeviceDTO> deviceDTOMap = deviceQueryService.getDeviceInfoByBatchId(deviceIdList);
        for (Map.Entry<String, DeviceDTO> entry : deviceDTOMap.entrySet()) {
            ipcIdList.add(entry.getValue().getSerial());
        }

        // 第一次循环时间设置
        String searchStartTime = startTime;
        String searchEndTime = startTime.substring(0, startTime.indexOf(" ")) + " 23:59:59";
        if (searchEndTime.compareTo(endTime) > 0) {
            searchEndTime = endTime;
        }

        do {
            StatisticsBean statisticsBean = new StatisticsBean();
            // 设置日期和抓拍数
            SearchResponse searchResponse = elasticSearchDao.getCaptureCount(searchStartTime, searchEndTime, ipcIdList);
            SearchHits searchHits = searchResponse.getHits();
            statisticsBean.setNumber(searchHits.getTotalHits() + "");
            statisticsBean.setGroupId(searchEndTime.substring(0, searchEndTime.indexOf(" ")));
            // 设置下次循环查询时间
            searchStartTime = searchEndTime;
            searchEndTime = DateUtils.getSpecifiedDayAfter(searchStartTime, OFFSET_DAY_ONE);
//            if (DateUtils.comparetor(searchEndTime, endTime) > 0) {
//                searchEndTime = endTime;
//            }
            statisticsBeanList.add(statisticsBean);
        } while (DateUtils.comparetor(searchStartTime, endTime) < 0);
        return statisticsBeanList;
    }

    /**
     * 根据ftpurl获取图片
     *
     * @param ftpUrl 图片地址
     * @return 图片数据
     */
    public String getImageBase64(String ftpUrl) {
        int substart = 6;
        int offset = 1;
        FTPClient ftpClient = new FTPClient();
        //FTP地址
        String ip = ftpUrl.substring(substart, ftpUrl.indexOf(":", substart));
        String port = ftpUrl.substring(ftpUrl.indexOf(":", substart) + offset, ftpUrl.indexOf("/", substart));
        // 设置IP，端口
        try {
            ftpClient.connect(ip, Integer.valueOf(port));
            // 登录
            ftpClient.login(FTP_NAME, FTP_PASSWORD);
            // 设置编码格式
            ftpClient.setControlEncoding("UTF-8");
            // 设置二进制文件传输方式
            ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
            // 进入对应目录
            ftpClient.changeWorkingDirectory(ftpUrl.substring(ftpUrl.indexOf("/", substart) + offset, ftpUrl.lastIndexOf("/")));
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            // 写入照片输出流
            ftpClient.retrieveFile(ftpUrl.substring(ftpUrl.lastIndexOf("/") + offset), os);
            byte[] bytes = os.toByteArray();
            // base64转化
            return new String(Base64.encodeBase64(bytes));
        } catch (IOException e) {
            log.error(e.getMessage());
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 通过入参确定起始和截止的时间，返回这段时间内的每一个小时的String
     *
     * @param startTime 开始时间
     * @param endTime   截止时间
     * @return 返回这段时间内的每一个小时的String
     */
    private List<String> getHourTime(String startTime, String endTime) {
        List<String> timeList = new ArrayList<>();
        Calendar start = Calendar.getInstance();
        DateFormat df = new SimpleDateFormat(EsSearchParam.TIMEFORMAT_YMDHMS);
        try {
            start.setTime(df.parse(startTime));
            Long startTimeL = start.getTimeInMillis();
            Calendar end = Calendar.getInstance();
            end.setTime(df.parse(endTime));
            Long endTimeL = end.getTimeInMillis();
            Long onehour = EsSearchParam.LONG_OBNEHOUR;
            Long time = startTimeL;
            while (time <= endTimeL) {
                Date everyTime = new Date(time);
                String timee = df.format(everyTime);
                timeList.add(timee);
                time += onehour;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return timeList;
    }
}