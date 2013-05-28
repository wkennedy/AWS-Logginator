package com.github.wkennedy.services

import org.jets3t.service.model.S3Object
import org.jets3t.service.{ServiceException, S3ServiceException}
import com.github.wkennedy.models.S3LogItem
import java.io.InputStream
import java.text.{ParseException, SimpleDateFormat}
import java.util.{Date, TimeZone}
import scala.collection.mutable.ListBuffer

class S3LogParsingService() extends RestS3ServiceTrait {

  def logEntryPattern = """(\S+) (\S+) \[(.*?)\] (\S+) (\S+) (\S+) (\S+) (\S+) "(.+?)" (\S+) (\S+) (\S+) (\S+) (\S+) (\S+) "(.*?)" "(.*?)" (\S)""".r
  val DEFAULT_PREFIX = "logs/"

//  val logBucket: String = logBucketName

  def getLogs(logBucket: String, prefix: String): Array[S3LogItem] = {
    //Get a list of all the files in the log bucket
    var logObjects: Array[S3Object] = null
    try {
      logObjects = RestS3Service().listObjects(logBucket, prefix, null)
    } catch {
      case e: S3ServiceException => logger.error("Error getting list objects from bucket", e)
        return new Array[S3LogItem](0)
    }

    //Filter the lines in the log files and populate the list of LogItem objects
    val logItems = populateLogItems(logBucket, logObjects)
    logger.info("Parsed the following entries from the logs:")
    logItems.foreach(logItem => logger.info(logItem.toString))

//    TODO
    //    Once we are finished parsing the logs, move them to an archive folder
    //    info_log.info("Archiving logs");
    //    archiveLogFiles(logObjects);

    logItems
  }

  def populateLogItems(logBucket: String, logObjects: Array[S3Object]): Array[S3LogItem] = {
    val listBuffer = new ListBuffer[S3LogItem]
    var count: Int = 1
    for (logObject <- logObjects) {
      var inputStream: InputStream = null
      try {
        val fullObject: S3Object = RestS3Service().getObject(logBucket, logObject.getKey)
        inputStream = fullObject.getDataInputStream
      }
      catch {
        case e: ServiceException => {
          logger.error("Error attempting to get data stream from object with key: " + logObject.getKey, e)
        }
      }
      val s3LogItems = scanLogInputStream(inputStream)
      logger.info("Finished processing log file " + count + " of " + logObjects.length)
      count += 1
      listBuffer.++=:(s3LogItems)
    }
    listBuffer.toArray
  }

  def scanLogInputStream(inputStream: InputStream): Array[S3LogItem] = {
    val logItems =
      scala.io.Source.fromInputStream(inputStream).getLines().toList.filter({
        line => logEntryPattern.pattern.matcher(line).matches()
      })

    val s3LogItems = new Array[S3LogItem](logItems.length)
    logItems.zipWithIndex foreach {
      case(logItem, logItemsIndex) =>
      val groups = logEntryPattern.unapplySeq(logItem).get
      val groupCount = groups.length
      val logItemStrings = new Array[String](groupCount)
      groups.zipWithIndex foreach {
        case(value, index) => logItemStrings(index) = value.toString
      }
      s3LogItems(logItemsIndex) = toLogItem(logItemStrings)
    }

    s3LogItems
  }

  def toLogItem(logItemGroups: Array[String]): S3LogItem = {
    val formatter: SimpleDateFormat = new SimpleDateFormat("dd/MMM/yyyy:HH:mm:ss Z")
    formatter.setTimeZone(TimeZone.getTimeZone("Etc/Zulu"))
    var date: Date = null
    try {
      date = formatter.parse(logItemGroups {2})
    } catch {
      case e: ParseException => {
        logger.error("Error parsing date for time: " + logItemGroups {
          3
        }, e)
      }
        return null
    }
    val time = date.getTime

    val s3LogItem = new S3LogItem()
    s3LogItem.bucketOwner = logItemGroups(0)
    s3LogItem.bucket = logItemGroups(1)
    s3LogItem.time = String.valueOf(time)
    s3LogItem.remoteIP = logItemGroups(3)
    s3LogItem.requester = logItemGroups(4)
    s3LogItem.requesterId = logItemGroups(5)
    s3LogItem.operation = logItemGroups(6)
    s3LogItem.key = logItemGroups(7)
    s3LogItem.requestUri = logItemGroups(8)
    s3LogItem.httpStatus =  logItemGroups(9)
    s3LogItem.errorCode = logItemGroups(10)
    s3LogItem.bytesSent = logItemGroups(11)
    s3LogItem.objectSize = logItemGroups(12)
    s3LogItem.totalTime = logItemGroups(13)
    s3LogItem.turnAroundTime = logItemGroups(14)
    s3LogItem.referrer = logItemGroups(15)
    s3LogItem.userAgent = logItemGroups(16)
    s3LogItem.versionId = logItemGroups(17)
    s3LogItem
  }
}
