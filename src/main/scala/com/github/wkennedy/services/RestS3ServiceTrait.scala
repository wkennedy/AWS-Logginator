package com.github.wkennedy.services

import org.jets3t.service.security.AWSCredentials
import org.jets3t.service.impl.rest.httpclient.RestS3Service
import org.jets3t.service.S3ServiceException
import org.slf4j.LoggerFactory


trait RestS3ServiceTrait {
  val logger = LoggerFactory.getLogger(getClass)

  def accessKey: String
  def secretKey: String

  def RestS3Service(): RestS3Service = {
    val awsCredentials = new AWSCredentials(accessKey, secretKey)
    try {
      new RestS3Service(awsCredentials)
    } catch {
      case e: S3ServiceException => logger.error("Error attempting to create new RestS3Service with AWS credentials", e)
        return null
    }
  }

}
