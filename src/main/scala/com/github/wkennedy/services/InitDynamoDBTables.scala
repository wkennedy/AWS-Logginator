package com.github.wkennedy.services

import com.amazonaws.auth.BasicAWSCredentials
import com.amazonaws.services.dynamodb.AmazonDynamoDBClient
import com.amazonaws.services.dynamodb.model._
import org.slf4j.LoggerFactory
import scala.InterruptedException
import com.amazonaws.AmazonServiceException

class InitDynamoDBTables {
  val logger = LoggerFactory.getLogger(getClass)

  var dynamoDBClient: AmazonDynamoDBClient = _

  def createDynamobDBLogEntryTable(accessKey: String, secretKey: String, tableName: String) {
    val credentials = new BasicAWSCredentials(accessKey, secretKey)
    dynamoDBClient = new AmazonDynamoDBClient(credentials)

    if (!isLogEntryTableCreated(tableName)) {
      createLogEntryTable(tableName)
    }
  }

  def isLogEntryTableCreated(tableName: String): Boolean = {
    val listTablesResult = dynamoDBClient.listTables()
    val tableNames = listTablesResult.getTableNames
    tableNames.contains(tableName)
  }

  def createLogEntryTable(tableName: String) {
    // Create a table with a primary (hash) key named 'bucket', which holds a string
    // and has a range key named 'time' that will be the time stamp of the log row in milliseconds
    val createTableRequest = new CreateTableRequest().withTableName(tableName)
      .withKeySchema(new KeySchema(new KeySchemaElement().withAttributeName("bucket").withAttributeType("S"))
      .withRangeKeyElement(new KeySchemaElement().withAttributeName("time").withAttributeType("N")))
      .withProvisionedThroughput(new ProvisionedThroughput().withReadCapacityUnits(10L).withWriteCapacityUnits(10L))
    val createdTableDescription = dynamoDBClient.createTable(createTableRequest).getTableDescription
    logger.info("Created Table: " + createdTableDescription)
    // Wait for it to become active
    waitForTableToBecomeAvailable(tableName)
  }

  def waitForTableToBecomeAvailable(tableName: String) {
    logger.info("Waiting for " + tableName + " to become ACTIVE...")

    val startTime = System.currentTimeMillis()
    val endTime = startTime + (10 * 60 * 1000)
    while (System.currentTimeMillis() < endTime) {
      try {
        Thread.sleep(1000 * 20)
      } catch {
        case e: InterruptedException => {
          logger.error("InterruptedException caught waiting on table creation for table: " + tableName, e)
        }
      }

      try {
        val request = new DescribeTableRequest().withTableName(tableName)
        val tableDescription = dynamoDBClient.describeTable(request).getTable
        val tableStatus = tableDescription.getTableStatus
        logger.info("  - current state: " + tableStatus)
        if (tableStatus.equals(TableStatus.ACTIVE.toString)) {
          return
        }
      } catch {
        case e: AmazonServiceException => {
          if (!e.getErrorCode.equalsIgnoreCase("ResourceNotFoundException")) {
            throw e
          }
        }
      }
    }

    throw new RuntimeException("Table " + tableName + " never became active")
  }

}
