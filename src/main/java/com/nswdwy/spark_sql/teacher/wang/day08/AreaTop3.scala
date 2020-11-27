package com.nswdwy.spark_sql.teacher.wang.day08

import org.apache.spark.SparkConf
import org.apache.spark.sql._
import org.apache.spark.sql.expressions.Aggregator

import scala.collection.mutable


object AreaTop3 {

  def main(args: Array[String]): Unit = {
        System.setProperty("HADOOP_USER_NAME", "yyc")
    val conf: SparkConf = new SparkConf().setMaster("local[*]").setAppName("SparkSQL")
    val spark: SparkSession = SparkSession.builder().enableHiveSupport().config(conf).getOrCreate()
        spark.sql("use default").show
        spark.udf.register("myaggr", functions.udaf(new CityAggregator))

        spark.sql(
          """
            |select
            |c.area,
            |p.product_name,
            |c.city_name,
            |u.click_product_id
            |
            |from
            |user_visit_action u
            |join
            |city_info c
            |on u.city_id = c.city_id
            |join
            |product_info p
            |on u.click_product_id = p.product_id
            |where u.click_product_id != -1;
            |""".stripMargin)
          .createTempView("t1")

        spark.sql(
          """
            |select
            |area,
            |product_name,
            |count(click_product_id) cnt,
            |myaggr(city_name) mark
            |
            |from t1
            |group by area,product_name
            |""".stripMargin)
          .createTempView("t2")

        spark.sql(
          """
            |select
            |*,
            |rank() over(partition by area order by cnt desc) rnk
            |from
            |t2
            |""".stripMargin)
          .createTempView("t3")

        spark.sql(
          """
            |select
            |area,product_name,cnt,mark
            |from
            |t3
            |where
            |rnk<=3
            |""".stripMargin).show

//    val u: DataFrame = spark.read.table("default.user_visit_action")
//    val c: DataFrame = spark.read.table("default.city_info")
//    val p: DataFrame = spark.read.table("default.product_info")

//    u.where(u("click_product_id") =!= -1)
//      .join(c, u("city_id") === c("city_id"))
//      .join(p, u("click_product_id") === p("product_id"))
//      .select(c("area"), p("product_name"), c("city_name"), u("click_product_id"))
//      .groupBy(c("area"), p("product_name"))
//      .agg(functions.count("click_product_id").alias("cnt"),
//        functions.udaf(new CityAggregator).apply(c("city_name")).alias("mark"))
//      .as[Table2]
//      .rdd.groupBy(x => x.area)
//      .mapValues(lines =>
//        lines.toList.sortBy(_.cnt)(Ordering.BigInt.reverse).take(3)
//      )
//      .flatMap(_._2)
//      .toDS.show

    /*//测试
    val t1: DataFrame = u.where(u("click_product_id") =!= -1)
      .join(c, u("city_id") === c("city_id"))
      .join(p, u("click_product_id") === p("product_id"))
      .select(c("area"), p("product_name"), c("city_name"), u("click_product_id"))

    t1.show

    t1.groupBy(c("area")).pivot("product_name").count.show*/
  }

}

case class Table2(area: String, product_name: String, cnt: BigInt, mark: String)

case class CityAggrBuffer(var totalClick: Long, map: mutable.Map[String, Int])

class CityAggregator extends Aggregator[String, CityAggrBuffer, String] {
  override def zero: CityAggrBuffer = {
    CityAggrBuffer(0L, mutable.Map.empty[String, Int])
  }

  override def reduce(b: CityAggrBuffer, a: String): CityAggrBuffer = {
    b.totalClick += 1
    b.map(a) = b.map.getOrElse(a, 0) + 1
    b
  }

  override def merge(b1: CityAggrBuffer, b2: CityAggrBuffer): CityAggrBuffer = {
    b1.totalClick += b2.totalClick
    b2.map.foreach {
      case (city, count) => b1.map(city) = b1.map.getOrElse(city, 0) + count
    }
    b1
  }

  override def finish(reduction: CityAggrBuffer): String = {
    val totalClick: Long = reduction.totalClick
    val citys: List[(String, Int)] = reduction.map.toList.sortBy(_._2)(Ordering.Int.reverse)
    val result: List[(String, Int)] = if (citys.length <= 3)
      citys
    else {
      val firstTwo: List[(String, Int)] = citys.take(2)
      firstTwo :+ ("其他", totalClick.toInt - citys(0)._2 - citys(1)._2)
    }

    result.map {
      case (city, count) => city + ": " + (count * 1000 / totalClick).toDouble / 10 + "%"
    }
      .mkString(",")
  }

  override def bufferEncoder: Encoder[CityAggrBuffer] = Encoders.product

  override def outputEncoder: Encoder[String] = Encoders.STRING
}
