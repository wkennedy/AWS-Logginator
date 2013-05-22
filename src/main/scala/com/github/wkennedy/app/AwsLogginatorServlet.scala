package com.github.wkennedy.app

import com.github.wkennedy.services.{Mapper, SimpleDBService}
import org.slf4j.LoggerFactory
import org.json4s.{DefaultFormats, Formats}
import org.scalatra.json._
import org.scalatra.MethodOverride

class AwsLogginatorServlet extends AwslogginatorStack with MethodOverride {

  protected implicit val jsonFormats: Formats = DefaultFormats

  val logger = LoggerFactory.getLogger(getClass)

  get("/") {
    render("index")
  }

//  get("/s3LogBuckets") {
//    contentType = formats("json")
//    val simpleDBService = new SimpleDBService
//    val s3LogBuckets = simpleDBService.getS3LogBuckets
//    val mapper = new Mapper
//    mapper.mapToS3LogBucket(s3LogBuckets)(0)
//  }

  get("/displayS3LogBuckets") {

  }

  get("/addLogBucket") {
    val simpleDBService = new SimpleDBService

    val s3LogBuckets = simpleDBService.getS3LogBuckets
    val mapper = new Mapper

    render("logBuckets", "logBuckets" -> mapper.mapToS3LogBucket(s3LogBuckets))
  }

  post("/saveS3LogBucket") {
    val bucketName = params("s3LogBucketName")
    val targetPrefix = params("s3LogBucketPrefix")
    val archive = params("s3LogBucketArchive")

    val simpleDBService = new SimpleDBService
    simpleDBService.saveS3LogBucket(bucketName, targetPrefix, archive)

    contentType="text/html"
    redirect("/addLogBucket")
  }

  delete("/deleteS3LogBucket/:name") {
    val simpleDBService = new SimpleDBService
    simpleDBService.deleteS3LogBucket(params("name"))
  }


}
