package com.github.wkennedy.services

import com.github.wkennedy.models.S3LogBucket
import com.amazonaws.services.simpledb.model.Item


class Mapper {

  def mapToS3LogBucket(items: List[Item]) : Array[S3LogBucket] = {
    val s3LogBuckets = new Array[S3LogBucket](items.length)

    items.view.zipWithIndex foreach{ case(value, index) =>
      val s3LogBucket = new S3LogBucket
      s3LogBucket.bucketName = value.getName
      s3LogBucket.targetPrefix = value.getAttributes.get(0).getValue
      s3LogBuckets(index) = s3LogBucket
    }
    s3LogBuckets
  }

}
