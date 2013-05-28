package com.github.wkennedy.models

class S3LogItem {
  var bucketOwner: String = _
  var bucket: String = _
  var time: String = _
  var remoteIP: String = _
  var requester: String = _
  var requesterId: String = _
  var operation: String = _
  var key: String = _
  var requestUri: String = _
  var httpStatus: String = _
  var errorCode: String = _
  var bytesSent: String = _
  var objectSize: String = _
  var totalTime: String = _
  var turnAroundTime: String = _
  var referrer: String = _
  var userAgent: String = _
  var versionId: String = _

  def getBucketOwner = bucketOwner

  def getBucket = bucket

  def getTime = time

  def getRemoteIP = remoteIP

  def getRequester = requester

  def getRequesterId = requesterId

  def getOperation = operation

  def getKey = key

  def getRequestUri = requestUri

  def getHttpStatus = httpStatus

  def getErrorCode = errorCode

  def getBytesSent = bytesSent

  def getObjectSize = objectSize

  def getTotalTime = totalTime

  def getTurnAroundTime = turnAroundTime

  def getReferrer = referrer

  def getUserAgent = userAgent

  def getVersionId = versionId

  def prefix: String = {
    if (key != null && !key.isEmpty && key.contains("/")) {
      return key.substring(0, key.indexOf("/"))
    }
    "-"
  }

  def getPrefix = prefix

  override def toString: String = {
    "LogItem" +
      "{bucketOwner=" + bucketOwner +
      ", bucket=" + bucket +
      ", prefix=" + prefix +
      ", time=" + time +
      ", remoteIP=" + remoteIP +
      ", requester=" + requester +
      ", requesterId=" + requesterId +
      ", operation=" + operation +
      ", key=" + key +
      ", requestUri=" + requestUri +
      ", httpStatus=" + httpStatus +
      ", errorCode=" + errorCode +
      ", bytesSent=" + bytesSent +
      ", objectSize=" + objectSize +
      ", totalTime=" + totalTime +
      ", turnAroundTime=" + turnAroundTime +
      ", referrer=" + referrer +
      ", userAgent=" + userAgent +
      ", versionId=" + versionId +
      "}"
  }
}
