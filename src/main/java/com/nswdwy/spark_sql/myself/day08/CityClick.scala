package com.nswdwy.spark_sql.myself.day08

import org.apache.spark.sql.Encoder
import org.apache.spark.sql.expressions.Aggregator

import scala.collection.mutable

/**
 * @author yycstart
 * @create 2020-11-04 11:36
 */
object CityClick {

}
case class CityAggrBuffer(var totalClick: Long, map: mutable.Map[String, Int])

class CityAggregator extends Aggregator[String, CityAggrBuffer, String] {
  override def zero: CityAggrBuffer = ???

  override def reduce(b: CityAggrBuffer, a: String): CityAggrBuffer = ???

  override def merge(b1: CityAggrBuffer, b2: CityAggrBuffer): CityAggrBuffer = ???

  override def finish(reduction: CityAggrBuffer): String = ???

  override def bufferEncoder: Encoder[CityAggrBuffer] = ???

  override def outputEncoder: Encoder[String] = ???
}
