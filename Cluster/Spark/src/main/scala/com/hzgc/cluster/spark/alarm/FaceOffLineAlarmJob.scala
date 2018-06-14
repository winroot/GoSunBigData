package com.hzgc.cluster.spark.alarm

import java.text.SimpleDateFormat
import java.util.Date

import com.google.gson.Gson
import com.hzgc.cluster.spark.dispatch.DeviceUtilImpl
import com.hzgc.cluster.spark.message.OffLineAlarmMessage
import com.hzgc.cluster.spark.rocmq.RocketMQProducer
import com.hzgc.cluster.spark.starepo.StaticRepoUtil
import com.hzgc.cluster.spark.util.PropertiesUtil
import com.hzgc.common.table.dispatch.DispatchTable
import com.hzgc.common.table.dynrepo.AlarmTable
import org.apache.spark.{SparkConf, SparkContext}

import scala.collection.JavaConverters

/**
  * 人脸识别离线告警任务（刘善彬）
  *
  */
object FaceOffLineAlarmJob {

  def main(args: Array[String]): Unit = {
    val properties = PropertiesUtil.getProperties
    val appName = properties.getProperty("job.offLine.appName")
    val mqTopic = properties.getProperty("rocketmq.topic.name")
    val nameServer = properties.getProperty("rocketmq.nameserver")
    val grouId = properties.getProperty("rocketmq.group.id")
    val esHost = properties.getProperty("es.hosts")
    val esPort = properties.getProperty("es.web.port")
    val conf = new SparkConf().setAppName(appName)
    conf.set("es.nodes", esHost).set("es.port", esPort)
    val sc = new SparkContext(conf)
    val deviceUtilImpl = new DeviceUtilImpl()
    val offLineAlarmRule = deviceUtilImpl.getThreshold
    val separator = "ZHONGXIAN"
    if (offLineAlarmRule != null && !offLineAlarmRule.isEmpty) {
      println("Start offline alarm task data processing ...")
      val objTypeList = PropertiesUtil.getOffLineArarmObjType(offLineAlarmRule)
      val returnResult = StaticRepoUtil.getInstance().searchByPkeysUpdateTime(objTypeList)
      if (returnResult != null && !returnResult.isEmpty) {
        val totalData = sc.parallelize(JavaConverters.asScalaBufferConverter(returnResult).asScala)
        val splitResult = totalData.map(totailDataElem => (totailDataElem.split(separator)(0), totailDataElem.split(separator)(1), totailDataElem.split(separator)(2)))
        val getDays = splitResult.map(splitResultElem => {
          val objRole = offLineAlarmRule.get(splitResultElem._2)
          if (objRole == null && objRole.isEmpty) {
            (splitResultElem._1, splitResultElem._2, splitResultElem._3, null)
          } else {
            val days = PropertiesUtil.getSimilarity(objRole)
            (splitResultElem._1, splitResultElem._2, splitResultElem._3, days)
          }
        }).filter(_._4 != null)
        val filterResult = getDays.filter(getFilter => getFilter._3 != null && getFilter._3.length != 0).
          map(getDaysElem => (getDaysElem._1, getDaysElem._2, getDaysElem._3, PropertiesUtil.timeTransition(getDaysElem._3), getDaysElem._4)).
          filter(ff => ff._4 != null && ff._4.length != 0).
          filter(filter => filter._4 > filter._5.toString)

        val df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        import org.elasticsearch.spark._
        val mapRdd = filterResult.map(record => {
          val pkey = record._2
          val alarmType = DispatchTable.OFFLINE
          val static_id = record._1
          val alarmTime = df.format(new Date())
          val map = Map(AlarmTable.ALARM_TYPE -> alarmType,
            AlarmTable.STATIC_ID -> static_id, AlarmTable.OBJECT_TYPE -> pkey,
            AlarmTable.LAST_APPEARANCE_TIME -> record._3,
            AlarmTable.ALARM_TIME -> alarmTime,
            AlarmTable.FLAG -> 0,
            AlarmTable.CONFIRM -> 1)
          map
        })
        mapRdd.saveToEs(AlarmTable.ALARM_INDEX + "/" + AlarmTable.OFF_ALARM_TYPE)
        //将离线告警信息推送到MQ()
        filterResult.foreach(filterResultElem => {
          val dateStr = df.format(new Date())
          val rocketMQProducer = RocketMQProducer.getInstance(nameServer, mqTopic, grouId)
          val offLineAlarmMessage = new OffLineAlarmMessage()
          val gson = new Gson()
          offLineAlarmMessage.setAlarmType(DispatchTable.OFFLINE.toString)
          offLineAlarmMessage.setStaticID(filterResultElem._1)
          offLineAlarmMessage.setUpdateTime(filterResultElem._3)
          offLineAlarmMessage.setAlarmTime(dateStr)
          val alarmStr = gson.toJson(offLineAlarmMessage)
          //离线告警信息推送的时候，平台id为对象类型字符串的前4个字节。
          //          val platID = filterResultElem._2.substring(0, 4)
          //由于平台那边取消了平台ID的概念,以后默认为0001
          rocketMQProducer.send("0001", "alarm_" + DispatchTable.OFFLINE.toString, filterResultElem._1 + dateStr, alarmStr.getBytes(), null)
        })
      } else {
        println("No data was received from the static repository,the task is not running！")
      }
    } else {
      println("No object type alarm dispatched offline,the task is not running")
    }
    sc.stop()
  }


}
