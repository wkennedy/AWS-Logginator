package com.github.wkennedy.app

import org.slf4j.LoggerFactory
import org.json4s.{DefaultFormats, Formats}
import org.scalatra.MethodOverride

class AwsLogginatorServlet extends AwslogginatorStack with MethodOverride {

  protected implicit val jsonFormats: Formats = DefaultFormats

  val logger = LoggerFactory.getLogger(getClass)

  get("/") {
    render("index")
  }

}
