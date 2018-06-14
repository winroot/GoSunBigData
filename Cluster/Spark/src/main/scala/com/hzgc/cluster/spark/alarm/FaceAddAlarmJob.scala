package com.hzgc.cluster.spark.alarm

import java.text.SimpleDateFormat
import java.util.Date

import com.google.gson.Gson
import com.hzgc.cluster.spark.dispatch.DeviceUtilImpl
import com.hzgc.cluster.spark.message.AddAlarmMessage
import com.hzgc.cluster.spark.rocmq.RocketMQProducer
import com.hzgc.cluster.spark.starepo.StaticRepoUtil
import com.hzgc.cluster.spark.util.{FaceObjectUtil, PropertiesUtil}
import com.hzgc.common.table.dispatch.DispatchTable
import com.hzgc.jni.FaceFunction
import kafka.serializer.StringDecoder
import org.apache.spark.SparkConf
import org.apache.spark.streaming.kafka.KafkaUtils
import org.apache.spark.streaming.{Durations, StreamingContext}

import scala.collection.JavaConverters
import scala.collection.mutable.ArrayBuffer

/**
  * 人脸新增告警实时处理任务（刘善彬）
  */
object FaceAddAlarmJob {

  case class Json(staticID: String,
                  staticObjectType: String,
                  sim: Float)

  def main(args: Array[String]): Unit = {
    val deviceUtilI = new DeviceUtilImpl()
    val properties = PropertiesUtil.getProperties
    val appName = properties.getProperty("job.addAlarm.appName")
    val timeInterval = Durations.seconds(properties.getProperty("job.addAlarm.timeInterval").toLong)
    val conf = new SparkConf()
      .setAppName(appName)
    val ssc = new StreamingContext(conf, timeInterval)
    val kafkaGroupId = properties.getProperty("kafka.FaceAddAlarmJob.group.id")
    val topics = Set(properties.getProperty("kafka.topic.name"))
    val mqTopic = properties.getProperty("rocketmq.topic.name")
    val nameServer = properties.getProperty("rocketmq.nameserver")
    val grouId = properties.getProperty("rocketmq.group.id")
    val brokers = properties.getProperty("kafka.metadata.broker.list")
    val kafkaParams = Map(
      "metadata.broker.list" -> brokers,
      "group.id" -> kafkaGroupId
    )
    val df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
    val kafkaDynamicPhoto = KafkaUtils.
      createDirectStream[String, String, StringDecoder, StringDecoder](ssc, kafkaParams, topics)

    val jsonResult = kafkaDynamicPhoto.map(data => (data._1, FaceObjectUtil.jsonToObject(data._2)))
      .filter(obj => obj._2.getAttribute.getFeature != null && obj._2.getAttribute.getFeature.length == 512)
      .map(obj => {
        val totalList = JavaConverters.
          asScalaBufferConverter(StaticRepoUtil.getInstance().getTotalList).asScala
        val faceObj = obj._2
        val ipcID = faceObj.getIpcId
        val alarmRule = deviceUtilI.isWarnTypeBinding(ipcID)
        val filterResult = new ArrayBuffer[Json]()
        if (alarmRule != null && !alarmRule.isEmpty) {
          val addWarnRule = alarmRule.get(DispatchTable.ADDED)
          if (addWarnRule != null && !addWarnRule.isEmpty) {
            totalList.foreach(record => {
              if (addWarnRule.containsKey(record(1))) {
                val threshold = FaceFunction.featureCompare(record(2).asInstanceOf[Array[Float]], faceObj.getAttribute.getFeature)
                if (threshold > addWarnRule.get(record(1)).toFloat) {
                  filterResult += Json(record(0).asInstanceOf[String], record(1).asInstanceOf[String], threshold)
                }
              }
            })
            val finalResult = filterResult.sortWith(_.sim > _.sim).take(3)
            //由于平台那边取消了平台ID的概念,以后默认为0001
            (obj._2, ipcID, "0001", finalResult)
          } else {
            println("Device [" + ipcID + "] does not bind added alarm rule,current time [" + df.format(new Date()) + "]")
            (obj._2, ipcID, null, filterResult)
          }
        } else {
          println("Device [" + ipcID + "] does not bind alarm rule,current time [" + df.format(new Date()) + "]")
          (obj._2, ipcID, null, filterResult)
        }
      }).filter(jsonResultFilter => jsonResultFilter._3 != null)

    jsonResult.foreachRDD(resultRDD => {
      resultRDD.foreachPartition(parRDD => {
        val rocketMQProducer = RocketMQProducer.getInstance(nameServer, mqTopic, grouId)
        val gson = new Gson()
        parRDD.foreach(result => {
          //识别集合为null，对该条数据进行新增告警。
          if (result._4 == null || result._4.isEmpty) {
            val dateStr = df.format(new Date())
            val addAlarmMessage = new AddAlarmMessage()
            val surl = result._1.getRelativePath
            addAlarmMessage.setAlarmTime(dateStr)
            addAlarmMessage.setAlarmType(DispatchTable.ADDED.toString)

            addAlarmMessage.setSmallPictureURL(surl)
            addAlarmMessage.setBigPictureURL(result._1.getBurl)
            addAlarmMessage.setDynamicDeviceID(result._2)
            addAlarmMessage.setHostName(result._1.getHostname)
            rocketMQProducer.send(result._3,
              "alarm_" + DispatchTable.ADDED.toString,
              surl,
              gson.toJson(addAlarmMessage).getBytes(),
              null)
          }
        })
      })
    })
    ssc.start()
    ssc.awaitTermination()
  }

}
