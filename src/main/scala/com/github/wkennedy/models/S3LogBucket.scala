package com.github.wkennedy.models

class S3LogBucket {

  var bucketName: String = _
  var targetPrefix: String = _
  var archivePrefix: String = _

  def getBucketName = bucketName
  def getTargetPrefix = targetPrefix
  def getArchivePrefix = archivePrefix
}
