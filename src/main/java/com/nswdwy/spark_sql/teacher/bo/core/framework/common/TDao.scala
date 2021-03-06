package com.nswdwy.spark_sql.teacher.bo.core.framework.common

import com.atguigu.bigdata.spark.core.framework.util.EnvUtil

trait TDao {

    def readFile(path:String) = {
        EnvUtil.take().textFile(path)
    }
}
