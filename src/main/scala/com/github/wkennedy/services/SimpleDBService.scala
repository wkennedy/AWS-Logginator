package com.github.wkennedy.services

import org.slf4j.LoggerFactory
import com.amazonaws.services.simpledb.AmazonSimpleDBClient
import com.amazonaws.auth.BasicAWSCredentials
import com.amazonaws.services.simpledb.model._
import scala.collection.JavaConversions._
import java.util

class SimpleDBService extends CredentialTrait {
  {
    val credentials = new BasicAWSCredentials(AWS_ACCESS_KEY, AWS_SECRET_KEY)
    simpleDBClient = new AmazonSimpleDBClient(credentials)
  }

  val logger = LoggerFactory.getLogger(getClass)

  def S3LogBucketsDomain = "S3LogBuckets"

  var simpleDBClient: AmazonSimpleDBClient = _

  def createSimpleDBDomain(domainName: String) {
    logger.info("Attempting to create domain with name: " + domainName)
    if(!isDomainCreated(domainName)) {
      createDomain(domainName)
    }
  }

  def isDomainCreated(domainName: String) : Boolean = {
    val listDomainsResult = simpleDBClient.listDomains()
    val domainNames = listDomainsResult.getDomainNames
    val created = domainNames.contains(domainName)
    logger.info("Does domain with name: " + domainName + " already exist: " + created)
    created
  }

  def createDomain(domainName: String) {
    val createDomainRequest : CreateDomainRequest = new CreateDomainRequest().withDomainName(domainName)
    simpleDBClient.createDomain(createDomainRequest)
    logger.info("Domain with name " + domainName + " was created")
  }

  def getS3LogBuckets : List[Item] = {
    val select = new SelectRequest("select * from " + S3LogBucketsDomain)
    val result = simpleDBClient.select(select)
    if(result != null) {
      return result.getItems.toList
    }
    List[Item]()
  }

  def saveS3LogBucket(s3LogBucket: String, prefix: String) {
    val attList : util.ArrayList[ReplaceableAttribute] = new util.ArrayList[ReplaceableAttribute]()
    attList.add( new ReplaceableAttribute().withName("bucketName").withValue(s3LogBucket))
    attList.add( new ReplaceableAttribute().withName("prefix").withValue(prefix))
    val putAttributeRequest : PutAttributesRequest = new PutAttributesRequest(S3LogBucketsDomain, s3LogBucket, attList)

    simpleDBClient.putAttributes(putAttributeRequest)
    logger.info("Saved new item to S3 Log Buckets domain with attributes s3LogBucket: " + s3LogBucket + " and prefix " + prefix)
  }
}
