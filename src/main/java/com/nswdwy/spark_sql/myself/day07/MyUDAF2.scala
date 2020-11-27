package com.nswdwy.spark_sql.myself.day07

import com.nswdwy.spark_sql.teacher.wang.day07.AvgBuffer
import org.apache.spark.sql.{Encoder, Encoders}
import org.apache.spark.sql.expressions.Aggregator


/**
 * @author yycstart
 * @create 2020-11-03 16:43
 */
object MyUDAF2 extends Aggregator[String,AvgBuffer,Double]{
  override def zero: AvgBuffer = {
    AvgBuffer(0L,0)
  }

  override def reduce(b: AvgBuffer, a: String): AvgBuffer = {
    b.sss += a.toLong
    b.count += 1
    b
  }

  override def merge(b1: AvgBuffer, b2: AvgBuffer): AvgBuffer = {
    b1.sss += b2.sss
    b1.count += b2.count
    b1
  }

  override def finish(reduction: AvgBuffer): Double = {
    reduction.sss.toDouble / reduction.count
  }

  override def bufferEncoder: Encoder[AvgBuffer] = Encoders.product

  override def outputEncoder: Encoder[Double] = Encoders.scalaDouble
}
