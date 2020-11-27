package com.nswdwy.spark_sql.teacher.wang.day08

import org.apache.spark.SparkConf
import org.apache.spark.sql.SparkSession

object TestSource {
  def main(args: Array[String]): Unit = {
//    System.setProperty("HADOOP_USER_NAME", "atguigu")
    val conf: SparkConf = new SparkConf().setMaster("local[*]").setAppName("SparkSQL")
    val spark: SparkSession = SparkSession.builder().enableHiveSupport().config(conf).getOrCreate()
    //import spark.implicits._

//    spark.read.format("json").option("multiLine", value = true).load("test.json").select("username")
//      .show
//
//    spark.read.json("")
//
//    spark.read.option("","").json()

//    val props: Properties = new Properties()
//    props.setProperty("user", "root")
//    props.setProperty("password", "000000")
//
//
//    spark.read.jdbc(
//      "jdbc:mysql://hadoop102:3306/ttt",
//      "user",
//      props
//    ).show

//    spark.read.format("jdbc")
//      .option("url","jdbc:mysql://hadoop102:3306/ttt")
//      .option("driver","com.mysql.jdbc.Driver")
//      .option("user","root")
//      .option("password","000000")
//      .option("dbtable","user")
//      .load().show

    spark.sql("show tables").show

    spark.sql("select * from student").show
  }
}
