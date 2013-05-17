import com.github.wkennedy.app._
import com.github.wkennedy.services.SimpleDBService
import org.scalatra._
import javax.servlet.ServletContext

class ScalatraBootstrap extends LifeCycle {

  implicit val swagger = new AwsLogginatorServlet

  override def init(context: ServletContext) {
    context.mount(new AwsLogginatorServlet, "/*")

//    System.setProperty("awsAccessKey", "")
//    System.setProperty("awsSecretKey", "+UIHmEjXOEcgWNu9")
    val simpleDBService = new SimpleDBService()
    simpleDBService.createSimpleDBDomain(simpleDBService.S3LogBucketsDomain)
  }
}
