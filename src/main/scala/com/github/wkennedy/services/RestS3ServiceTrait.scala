package com.github.wkennedy.services

import org.jets3t.service.security.AWSCredentials
import org.jets3t.service.impl.rest.httpclient.RestS3Service
import org.jets3t.service.S3ServiceException
import org.slf4j.LoggerFactory


trait RestS3ServiceTrait extends CredentialTrait {
  val logger = LoggerFactory.getLogger(getClass)

  def RestS3Service(): RestS3Service = {
    val awsCredentials = new AWSCredentials(AWS_ACCESS_KEY, AWS_SECRET_KEY)
    try {
      new RestS3Service(awsCredentials)
    } catch {
      case e: S3ServiceException => logger.error("Error attempting to create new RestS3Service with AWS credentials", e)
        return null
    }
  }

}
