package com.github.wkennedy.app

import org.scalatra._
import scalate.ScalateSupport
import com.github.wkennedy.services.S3LogParsingService

class AwsLogginatorServlet extends AwslogginatorStack {

  get("/") {
    <html>
      <body>
        <h1>Hello, world!</h1>
        Say <a href="hello-scalate">hello to Scalate</a>.
      </body>
    </html>
  }
  
}
