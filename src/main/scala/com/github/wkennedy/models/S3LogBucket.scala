package com.github.wkennedy.models

class S3LogBucket {

  var bucketName: String = _
  var targetPrefix: String = _

  def getBucketName = bucketName
  def getTargetPrefix = targetPrefix
}
