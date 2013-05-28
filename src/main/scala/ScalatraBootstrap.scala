import _root_.akka.actor.ActorSystem
import com.github.wkennedy.app._
import com.github.wkennedy.services._
import org.scalatra._
import javax.servlet.ServletContext
import scala.concurrent.duration._
import scala.concurrent.ExecutionContext.Implicits.global

class ScalatraBootstrap extends LifeCycle {

  implicit val swagger = new AwsLogginatorServlet

  // Get a handle to an ActorSystem and a reference to one of your actors
  val actorSystem = ActorSystem()
  val logBucketService = new LogBucketService()
  val simpleDBService = new SimpleDBService()
  val dynamoDBService = new DynamoDBService()

  override def init(context: ServletContext) {
    System.setProperty("awsAccessKey", "AKIAJ5KSEFHFPQYFMLHA")
    System.setProperty("awsSecretKey", "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")

    context.mount(new AwsLogginatorServlet, "/*")
    context.mount(new LogBucketServlet, "/logBucket")
    context.mount(new S3LogItemsServlet, "/s3LogItems")

    dynamoDBService.createDynamobDBLogEntryTable("aws-logginator-s3-logs")
    simpleDBService.createSimpleDBDomain("S3LogBuckets")

    actorSystem.scheduler.schedule(0 minutes, 5 minutes) {
      println("Starting scheduler")
      startParsingLogs()
    }
  }

  def startParsingLogs() {
    val s3LogParsingService = new S3LogParsingService()
    val s3LoggingService = new S3LoggingService()

    val s3LogBucketItems = logBucketService.getS3LogBuckets
    val mapper = new Mapper

    val s3LogBuckets = mapper.mapToS3LogBucket(s3LogBucketItems)
    for (s3LogBucket <- s3LogBuckets) {
      val s3LogItems = s3LogParsingService.getLogs(s3LogBucket.getBucketName, s3LogBucket.getTargetPrefix)
      s3LoggingService.saveLogItems(s3LogItems)
    }
  }

  override def destroy(context: ServletContext) {
    actorSystem.shutdown()
  }
}
