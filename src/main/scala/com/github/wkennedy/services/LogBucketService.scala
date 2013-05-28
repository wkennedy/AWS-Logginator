package com.github.wkennedy.services

import com.amazonaws.services.simpledb.model._
import java.util
import scala.collection.JavaConversions._

class LogBucketService extends SimpleDBService {

  object LogBucket {
    def S3LogBucketsDomain = "S3LogBuckets"
  }

  def getS3LogBuckets : List[Item] = {
    val select = new SelectRequest("select * from " + LogBucket.S3LogBucketsDomain)
    val result = simpleDBClient.select(select)
    if(result != null) {
      return result.getItems.toList
    }
    List[Item]()
  }

  def saveS3LogBucket(s3LogBucket: String, prefix: String, archive: String) {
    val attList : util.ArrayList[ReplaceableAttribute] = new util.ArrayList[ReplaceableAttribute]()
    attList.add( new ReplaceableAttribute().withName("bucketName").withValue(s3LogBucket))
    attList.add( new ReplaceableAttribute().withName("prefix").withValue(prefix))
    attList.add( new ReplaceableAttribute().withName("archive").withValue(archive))
    val putAttributeRequest : PutAttributesRequest = new PutAttributesRequest(LogBucket.S3LogBucketsDomain, s3LogBucket, attList)

    simpleDBClient.putAttributes(putAttributeRequest)
    logger.info("Saved new item to S3 Log Buckets domain with attributes s3LogBucket: " + s3LogBucket + " and prefix " + prefix)
  }

  def deleteS3LogBucket(itemName: String) {
    simpleDBClient.deleteAttributes(new DeleteAttributesRequest(LogBucket.S3LogBucketsDomain, itemName))
  }

}
