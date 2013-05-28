package com.github.wkennedy.services

import org.slf4j.LoggerFactory
import com.amazonaws.services.simpledb.AmazonSimpleDBClient
import com.amazonaws.auth.BasicAWSCredentials
import com.amazonaws.services.simpledb.model._

class SimpleDBService extends CredentialTrait {
  {
    val credentials = new BasicAWSCredentials(AWS_ACCESS_KEY, AWS_SECRET_KEY)
    simpleDBClient = new AmazonSimpleDBClient(credentials)
  }

  val logger = LoggerFactory.getLogger(getClass)

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

  def query(query: String) : SelectResult = {
    val select = new SelectRequest(query)
    simpleDBClient.select(select)
  }

  def createDomain(domainName: String) {
    val createDomainRequest : CreateDomainRequest = new CreateDomainRequest().withDomainName(domainName)
    simpleDBClient.createDomain(createDomainRequest)
    logger.info("Domain with name " + domainName + " was created")
  }

}
