import io.vertx.lang.scala.ScalaVerticle
import io.vertx.scala.core.eventbus.Message

import scala.concurrent.{Future, Promise}

class MailMessenger extends ScalaVerticle {

  override def startFuture(): Future[Unit] = {
    println("starting")
    val eb = vertx.eventBus()

    eb.consumer("helicarrier-channel", (message : Message[String]) => {
      println(s"Mail messenger received a message: ${message.body()}")
    })
    
    Promise[Unit]().future
  }
}
