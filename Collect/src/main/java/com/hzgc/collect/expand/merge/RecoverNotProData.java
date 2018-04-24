package com.hzgc.collect.expand.merge;

import com.hzgc.collect.expand.conf.CommonConf;
import com.hzgc.collect.expand.log.LogEvent;
import com.hzgc.collect.expand.util.KafkaProperties;
import com.hzgc.collect.expand.util.ProducerKafka;
import com.hzgc.common.ftp.faceobj.FaceObject;
import com.hzgc.common.util.json.JSONUtil;
import org.apache.log4j.Logger;

import java.util.List;

/**
 * 恢复未处理的数据(曹大报)
 * 整体流程：
 * 1，根据CommonConf获取process根路径processLogDir；
 * 2，遍历process 目录中的所有文件，得到日记文件的一个绝对路径，放入一个List中，processFiles；
 *      判断 processFiles 是否为空(process 目录下是否有文件)；
 *          1) processFiles不为空，遍历获取 processFiles 中每一个元素(process文件的绝对路径)，例如：/opt/logdata/process/p-0/000003000000.log；
 *              根据process文件获取receive文件路径，判断对应receive文件(/opt/logdata/receive/r-0/000003000000.log)是否存在；
 *                      A、存在：
 *                              1，读取process文件和receive文件，合并到一个List 里面(rowsListFactory)；
 *                              2，排序，对比，获取有序的数据集合 notProRows；
 *                              3，遍历 notProRows 每一条数据，提取特征获取faceObject发送Kafka；
 *                                  发送kafka后，写入到process下的日志文件中；
 *                                          发送kafka的数据少于未处理的数据数，则失败，结束循环返回false；
 *                      B、不存在：
 *                              把/opt/logdata/process/p-0/000003000000.log 文件移动到success目录下，跳过这层循环。
 *          2）processFiles 为空
 *              结束循环；
 * 3，结束
 */
public class RecoverNotProData {

    private Logger LOG = Logger.getLogger(RecoverNotProData.class);

    private String feature = KafkaProperties.getTopicFeature();

    public boolean recoverNotProData(CommonConf commonConf) {
        MergeUtil mergeUtil = new MergeUtil();
        String processLogDir = commonConf.getProcessLogDir();
        //获取正在写的日志队列文件
        String writingLogFile = commonConf.getLogName();
        FileFactory fileFactory = new FileFactory(processLogDir, writingLogFile);
        //获取processLogDir目录下除去error日志所有文件绝对路径
        List<String> processFiles = fileFactory.getAllProcessLogAbsPath();
        //获取processLogDir目录下除去最大,error和000.log的绝对路径
        List<String> backupLogAbsPath = fileFactory.getAllBackupLogAbsPath();

        //判断process根目录下是否有文件
        if (processFiles != null) {
            for (String processFile : processFiles) {
                //获取receive绝对路径
                String receiveFile = mergeUtil.getRecFilePathFromProFile(processFile);
                //判断对应receive文件是否存在，存在则合并，不存在则移动位置
                if (mergeUtil.isFileExist(receiveFile)) {
                    RowsListFactory rowsListFactory = new RowsListFactory(processFile, receiveFile);
                    //获取未处理的数据
                    List<String> notProRows = rowsListFactory.getNotProRows();

                    //用于标记发送Kafka数据数
                    long rowCount = 0;
                    ProducerKafka producerKafka = ProducerKafka.getInstance();
                    for (String row : notProRows) {
                        //获取未处理数据的ftpUrl
                        LogEvent event = JSONUtil.toObject(row, LogEvent.class);
                        String ftpUrl = event.getFtpPath();
                        FaceObject faceObject = GetFaceObject.getFaceObject(row);
                        if (faceObject != null) {
                            //发送Kafka后,将日志写到process中
                            MergeSendCallback mergeSendCallback = new MergeSendCallback(ftpUrl);
                            producerKafka.sendKafkaMessage(feature, ftpUrl, faceObject, mergeSendCallback);
                            mergeUtil.writeMergeFile(event, processFile);
                        }
                        rowCount++;
                    }
                    if (rowCount == notProRows.size()) {
                        //处理process文件完成，移动process文件和receive文件到success目录下；
                        if (backupLogAbsPath.contains(processFile)) {
                            String sucProFilePath = mergeUtil.getSuccessFilePath(processFile);
                            mergeUtil.moveFile(processFile, sucProFilePath);
                            String sucRecFilePath = mergeUtil.getSuccessFilePath(receiveFile);
                            mergeUtil.moveFile(receiveFile, sucRecFilePath);
                        }
                    } else {
                        LOG.warn("send to Kafka data less than NotProRows size, Please check it!");
                        return false;
                    }
                } else {
                    //对应receive 文件不存在，将process文件移动到success目录下
                    LOG.info("Can't find receiveFile,move processFile To SuccessDir");
                    String successFilePath = mergeUtil.getSuccessFilePath(processFile);
                    mergeUtil.moveFile(processFile, successFilePath);
                }
            }
        } else {
            LOG.info("The path of " + processLogDir + "is Nothing");
        }
        return true;
    }
}




