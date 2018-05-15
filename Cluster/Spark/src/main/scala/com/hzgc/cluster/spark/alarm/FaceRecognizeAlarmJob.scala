package com.hzgc.cluster.spark.alarm

import java.text.SimpleDateFormat
import java.util
import java.util.Date

import com.google.gson.Gson
import com.hzgc.cluster.spark.device.DeviceUtilImpl
import com.hzgc.cluster.spark.message.{Item, RecognizeAlarmMessage}
import com.hzgc.cluster.spark.rocmq.RocketMQProducer
import com.hzgc.cluster.spark.starepo.ObjectInfoInnerHandlerImpl
import com.hzgc.cluster.spark.util.{FaceObjectUtil, PropertiesUtils}
import com.hzgc.common.table.device.DeviceTable
import com.hzgc.jni.FaceFunction
import kafka.serializer.StringDecoder
import org.apache.spark.SparkConf
import org.apache.spark.streaming.kafka.KafkaUtils
import org.apache.spark.streaming.{Durations, StreamingContext}

import scala.collection.JavaConverters
import scala.collection.mutable.ArrayBuffer


/**
  * 人脸识别告警实时计算任务
  */
object FaceRecognizeAlarmJob {

  case class Json(staticID: String,
                  staticObjectType: String,
                  sim: Float)

  def main(args: Array[String]): Unit = {
    val deviceUtilI = new DeviceUtilImpl()
    val properties = PropertiesUtils.getProperties
    val appName = properties.getProperty("job.recognizeAlarm.appName")
    val mqTopic = properties.getProperty("rocketmq.topic.name")
    val nameServer = properties.getProperty("rocketmq.nameserver")
    val grouId = properties.getProperty("rocketmq.group.id")
    val itemNum = properties.getProperty("job.recognizeAlarm.items.num").toInt
    val timeInterval = Durations.seconds(properties.getProperty("job.recognizeAlarm.timeInterval").toLong)
    val conf = new SparkConf()
      .setAppName(appName)
    val ssc = new StreamingContext(conf, timeInterval)

    val kafkaGroupId = properties.getProperty("kafka.FaceRecognizeAlarmJob.group.id")
    val topics = Set(properties.getProperty("kafka.topic.name"))
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
      .map(message => {
        val totalList = JavaConverters.
          asScalaBufferConverter(ObjectInfoInnerHandlerImpl.getInstance().getTotalList).asScala
        val faceObj = message._2
        val ipcID = faceObj.getIpcId
        val platID = deviceUtilI.getplatfromID(ipcID)
        val alarmRule = deviceUtilI.isWarnTypeBinding(ipcID)
        val filterResult = new ArrayBuffer[Json]()
        if (platID != null && platID.length > 0) {
          if (alarmRule != null && !alarmRule.isEmpty) {
            val recognizeWarnRule = alarmRule.get(DeviceTable.IDENTIFY)
            if (recognizeWarnRule != null && !recognizeWarnRule.isEmpty) {
              totalList.foreach(record => {
                if (recognizeWarnRule.containsKey(record(1))) {
                  val threshold = FaceFunction.featureCompare(record(2).asInstanceOf[Array[Float]], faceObj.getAttribute.getFeature)
                  if (threshold > recognizeWarnRule.get(record(1))) {
                    filterResult += Json(record(0).asInstanceOf[String], record(1).asInstanceOf[String], threshold)
                  }
                }
              })
            } else {
              println("Device [" + ipcID + "] does not bind recognize rule,current time [" + df.format(new Date()) + "]")
            }
          } else {
            println("Device [" + ipcID + "] does not bind alarm rules,current time [" + df.format(new Date()) + "]")
          }
        } else {
          println("Device [" + ipcID + "] does not bind platform ID,current time [" + df.format(new Date()) + "]")
        }
        val finalResult = filterResult.sortWith(_.sim > _.sim).take(itemNum)
        val updateTimeList = new util.ArrayList[String]()
        if (alarmRule != null && platID != null) {
          val offLineWarnRule = alarmRule.get(DeviceTable.OFFLINE)
          if (offLineWarnRule != null && !offLineWarnRule.isEmpty) {
            finalResult.foreach(record => {
              if (offLineWarnRule.containsKey(record.staticObjectType)) {
                updateTimeList.add(record.staticID)
              }
            })
          }
        }
        ObjectInfoInnerHandlerImpl.getInstance().updateObjectInfoTime(updateTimeList)
        (message._2, ipcID, platID, finalResult)
      }).filter(record => record._4.nonEmpty)

    jsonResult.foreachRDD(resultRDD => {
      resultRDD.foreachPartition(parRDD => {
        val rocketMQProducer = RocketMQProducer.getInstance(nameServer, mqTopic, grouId)
        val gson = new Gson()
        parRDD.foreach(result => {
          val recognizeAlarmMessage = new RecognizeAlarmMessage()
          val items = new ArrayBuffer[Item]()
          val dateStr = df.format(new Date())
          val surl = result._1.getSurl
          recognizeAlarmMessage.setAlarmType(DeviceTable.IDENTIFY.toString)
          recognizeAlarmMessage.setDynamicDeviceID(result._2)
          recognizeAlarmMessage.setSmallPictureURL(surl)
          recognizeAlarmMessage.setAlarmTime(dateStr)
          recognizeAlarmMessage.setBigPictureURL(result._1.getBurl)
          recognizeAlarmMessage.setHostName(result._1.getHostname)
          result._4.foreach(record => {
            val item = new Item()
            item.setSimilarity(record.sim.toString)
            item.setStaticID(record.staticID)
            item.setObjType(record.staticObjectType)
            items += item
          })
          recognizeAlarmMessage.setItems(items.toArray)
          rocketMQProducer.send(result._3,
            "alarm_" + DeviceTable.IDENTIFY.toString,
            surl,
            gson.toJson(recognizeAlarmMessage).getBytes(),
            null)
        })
      })
    })
    ssc.start()
    ssc.awaitTermination()
  }
}