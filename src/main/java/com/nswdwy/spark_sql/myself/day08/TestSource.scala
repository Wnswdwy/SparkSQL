package com.nswdwy.spark_sql.myself.day08

import org.apache.spark.sql.SparkSession
import org.apache.spark.{SparkConf, SparkContext}

/**
 * @author yycstart
 * @create 2020-11-04 10:52
 */
object TestSource {
  def main(args: Array[String]): Unit = {
    System.setProperty("HADOOP_USER_NAME", "yyc")
    val conf: SparkConf = new SparkConf().setMaster("local[*]").setAppName("SparkzSQL")
    //创建SparkSession
    val spark: SparkSession = SparkSession
      .builder()
      .enableHiveSupport()
      .config(conf)
      .getOrCreate()

    spark.sql("show tables").show()
//    spark.sql("create table if not exists stu1 (id int,name String) ")
//    spark.sql("show tables").show()
        spark.sql("select * from default.student").show()
        spark.sql("select * from default.user_visit_action limit 10").show()

  }
}
