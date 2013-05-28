package com.github.wkennedy.services

import com.github.wkennedy.models.{S3LogItem, S3LogBucket}
import com.amazonaws.services.simpledb.model.{Attribute, Item}
import scala.collection.JavaConversions._
import com.amazonaws.services.dynamodb.model.ScanResult


class Mapper {

  def mapToS3LogBucket(items: List[Item]): Array[S3LogBucket] = {
    val s3LogBuckets = new Array[S3LogBucket](items.length)

    items.view.zipWithIndex foreach {
      case (value, index) =>
        val s3LogBucket = new S3LogBucket
        s3LogBucket.bucketName = value.getName
        val attributes = value.getAttributes.toList
        for (attribute: Attribute <- attributes) {
          if (attribute.getName.equals("prefix")) {
            s3LogBucket.targetPrefix = attribute.getValue
          } else if (attribute.getName.equals("archive")) {
            s3LogBucket.archivePrefix = attribute.getValue
          }
        }
        s3LogBuckets(index) = s3LogBucket
    }
    s3LogBuckets
  }

  def mapToS3LogItem(result: ScanResult): Array[S3LogItem] = {
    val items = result.getItems
    val s3LogItems = new Array[S3LogItem](items.size())
    items.view.zipWithIndex foreach {
      case (value, index) =>
        val s3LogItem = new S3LogItem()
        s3LogItem.bucketOwner = value.get("bucket_owner").getS
        s3LogItem.bucket = value.get("bucket").getS
        s3LogItem.time = value.get("time").getN
        s3LogItem.remoteIP = value.get("remote_ip").getS
        s3LogItem.requester = value.get("requester").getS
        s3LogItem.requesterId = value.get("requester_id").getS
        s3LogItem.operation = value.get("operation").getS
        s3LogItem.key = value.get("key").getS
        s3LogItem.requestUri = value.get("request_uri").getS
        s3LogItem.httpStatus = value.get("http_status").getS
        s3LogItem.errorCode = value.get("error_code").getS
        s3LogItem.bytesSent = value.get("bytes_sent").getN
        s3LogItem.objectSize = value.get("object_size").getN
        s3LogItem.totalTime = value.get("total_time").getS
        s3LogItem.turnAroundTime = value.get("turn_around_time").getS
        s3LogItem.referrer = value.get("referrer").getS
        s3LogItem.userAgent = value.get("user_agent").getS
        s3LogItem.versionId = value.get("version_id").getS
        s3LogItems(index) = s3LogItem
    }
    s3LogItems
  }

}
