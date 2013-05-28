package com.github.wkennedy.services

import com.amazonaws.auth.BasicAWSCredentials
import com.github.wkennedy.models.S3LogItem
import scala.collection.mutable
import com.amazonaws.services.dynamodb.model.{PutItemRequest, AttributeValue}
import com.amazonaws.services.dynamodb.AmazonDynamoDBClient
import org.slf4j.LoggerFactory
import collection.JavaConverters._

class S3LoggingService extends CredentialTrait {
  {
    val credentials = new BasicAWSCredentials(AWS_ACCESS_KEY, AWS_SECRET_KEY)
    dynamoDBClient = new AmazonDynamoDBClient(credentials)
  }

  val logger = LoggerFactory.getLogger(getClass)

  var dynamoDBClient: AmazonDynamoDBClient = _

  def saveLogItems(logItems: Array[S3LogItem]) {
    for (logItem <- logItems) {
      saveLogItem(logItem)
    }
  }

  def saveLogItem(logItem: S3LogItem) {
    val logItemAttribute = toLogItemAttribute(logItem)
    val putItemRequest : PutItemRequest = new PutItemRequest("aws-logginator-s3-logs", logItemAttribute.toMap.asJava)
    try {
      dynamoDBClient.putItem(putItemRequest)
    } catch {
      case e: Exception => {
        logger.error("Exception caught putting the following log item into table " + "aws-logginator-s3-logs" + ": " + logItem.toString(), e)
      }
    }
  }

  def toLogItemAttribute(logItem: S3LogItem) : mutable.HashMap[String, AttributeValue] = {
    val item = mutable.HashMap.empty[String, AttributeValue]
    item += "bucket_owner" -> new AttributeValue(logItem.bucketOwner)
    item += "bucket_prefix" ->new AttributeValue(logItem.prefix)
    item += "bucket" -> new AttributeValue(logItem.bucket)

    var time = "0"
    if(!"-".equals(logItem.time)) {
      time = logItem.time
    }
    item += "time" -> new AttributeValue().withN(time)
    item += "remote_ip" -> new AttributeValue(logItem.remoteIP)
    item += "requester" -> new AttributeValue(logItem.requester)
    item += "requester_id" -> new AttributeValue(logItem.requesterId)
    item += "operation" -> new AttributeValue(logItem.operation)
    item += "key" -> new AttributeValue(logItem.key)
    item += "request_uri" -> new AttributeValue(logItem.requestUri)
    item += "http_status" -> new AttributeValue(logItem.httpStatus)
    item += "error_code" -> new AttributeValue(logItem.errorCode)

    var bytesSent = "0"
    if(!"-".equals(logItem.bytesSent)) {
      bytesSent = logItem.bytesSent
    }
    item += "bytes_sent" -> new AttributeValue().withN(bytesSent)

    var objectSize = "0"
    if(!"-".equals(logItem.objectSize)) {
      objectSize = logItem.objectSize
    }
    item += "object_size" -> new AttributeValue().withN(objectSize)
    item += "total_time" -> new AttributeValue(logItem.totalTime)
    item += "turn_around_time" -> new AttributeValue(logItem.turnAroundTime)
    item += "referrer" -> new AttributeValue(logItem.referrer)
    item += "user_agent" -> new AttributeValue(logItem.userAgent)
    item += "version_id" -> new AttributeValue(logItem.versionId)

    item
  }
}
