package com.github.wkennedy.app

import org.scalatra.MethodOverride
import com.github.wkennedy.services.{LogBucketService, Mapper}
import org.json4s.{DefaultFormats, Formats}

class LogBucketServlet extends AwslogginatorStack with MethodOverride {

  val logBucketService = new LogBucketService

  protected implicit val jsonFormats: Formats = DefaultFormats

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
    val s3LogBuckets = logBucketService.getS3LogBuckets
    val mapper = new Mapper

    render("logBuckets", "logBuckets" -> mapper.mapToS3LogBucket(s3LogBuckets))
  }

  post("/saveS3LogBucket") {
    val bucketName = params("s3LogBucketName")
    val targetPrefix = params("s3LogBucketPrefix")
    val archive = params("s3LogBucketArchive")

    logBucketService.saveS3LogBucket(bucketName, targetPrefix, archive)

    contentType="text/html"
    redirect("/logBucket/addLogBucket")
  }

  delete("/deleteS3LogBucket/:name") {
    logBucketService.deleteS3LogBucket(params("name"))
  }

}
