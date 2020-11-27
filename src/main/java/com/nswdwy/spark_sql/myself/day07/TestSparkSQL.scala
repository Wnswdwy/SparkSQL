package com.nswdwy.spark_sql.myself.day07

import com.nswdwy.spark_sql.teacher.wang.day07.UserVisitAction
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.{DataFrame, Dataset, Row, SparkSession}
import org.apache.spark.{SparkConf, SparkContext}


/**
 * @author yycstart
 * @create 2020-11-03 16:03
 */
object TestSparkSQL {
  def main(args: Array[String]): Unit = {
    val conf: SparkConf = new SparkConf().setMaster("local[*]").setAppName("Spark")
    val sc: SparkContext = new SparkContext(conf)
    val session: SparkSession = SparkSession.builder().config(conf).getOrCreate()

    import session.implicits._
    val function: MyUDAF = new MyUDAF

    session.udf.register("myavg",function)

    val actionRDD: RDD[UserVisitAction] = sc.textFile("user_visit_action.txt")
      .map(_.split("_"))
      .map(
        data=> UserVisitAction(
            data(0),
            data(1),
            data(2),
            data(3),
            data(4),
            data(5),
            data(6),
            data(7),
            data(8),
            data(9),
            data(10),
            data(11),
            data(12),
        ))

    val ds: Dataset[UserVisitAction] = actionRDD.toDS
    ds.createTempView("uva")
//    ds.filter(x=>x.city_id.toInt > 10).show

    val df: DataFrame = actionRDD.toDF
    val rd1: RDD[Row] = df.rdd
    val rd2: RDD[UserVisitAction] = ds.rdd
    val df2: DataFrame = ds.toDF()
    val ds2: Dataset[UserVisitAction] = df.as[UserVisitAction]
    session.sql("select myavg(page_id) from uva").show

  }
}
