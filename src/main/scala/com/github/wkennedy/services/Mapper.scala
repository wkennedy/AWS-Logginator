package com.github.wkennedy.services

import com.github.wkennedy.models.S3LogBucket
import com.amazonaws.services.simpledb.model.{Attribute, Item}
import scala.collection.JavaConversions._


class Mapper {

  def mapToS3LogBucket(items: List[Item]) : Array[S3LogBucket] = {
    val s3LogBuckets = new Array[S3LogBucket](items.length)

    items.view.zipWithIndex foreach{ case(value, index) =>
      val s3LogBucket = new S3LogBucket
      s3LogBucket.bucketName = value.getName
      val attributes = value.getAttributes.toList
      for(attribute: Attribute <- attributes) {
        if(attribute.getName.equals("prefix")) {
          s3LogBucket.targetPrefix = attribute.getValue
        } else if(attribute.getName.equals("archive")) {
          s3LogBucket.archivePrefix = attribute.getValue
        }
      }
      s3LogBuckets(index) = s3LogBucket
    }
    s3LogBuckets
  }

}
