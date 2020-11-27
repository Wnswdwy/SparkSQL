package com.nswdwy.spark_sql.myself.day07

import org.apache.spark.SparkConf
import org.apache.spark.sql.{DataFrame, Dataset, SparkSession}


/**
 * @author yycstart
 * @create 2020-11-03 21:13
 */
object SparkSQL_Basic01 {
  def main(args: Array[String]): Unit = {
    val conf: SparkConf = new SparkConf().setMaster("local[*]").setAppName("sparkSQL")
    val spark: SparkSession = SparkSession.builder().config(conf).getOrCreate()
    val df: DataFrame = spark.read.json("datas/test.json")
//    df.show()

      //DataFrame => SQL
//    df.createTempView("user")
//    spark.sql("select age,username from user").show()

    //DataFrame => DSL
//    df.select("age","username").show
    //设计转换规则需要引入转换规则
//
//    df.select('age + 1).show()


    val seq: Seq[Int] = Seq(1, 2, 3, 4)

//    val ds:Dataset[Int] = seq.toDS()
    spark.close()
  }
}
