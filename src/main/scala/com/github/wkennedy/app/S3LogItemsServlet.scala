package com.github.wkennedy.app

import org.scalatra.MethodOverride
import org.json4s.{DefaultFormats, Formats}
import com.github.wkennedy.services.DynamoDBService

class S3LogItemsServlet extends AwslogginatorStack with MethodOverride {

  protected implicit val jsonFormats: Formats = DefaultFormats

  val dynamoDBService = new DynamoDBService()

  get("/s3LogItems") {
    render("s3LogItems", "s3LogItems" -> dynamoDBService.getS3LogItems)
  }

}
