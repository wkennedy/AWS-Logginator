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

  override def toString : String = {
    "LogItem" +
    "{bucketOwner=" + bucketOwner + 
    ", bucket=" + bucket + 
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
