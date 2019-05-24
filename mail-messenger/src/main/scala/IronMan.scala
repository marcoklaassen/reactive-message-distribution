import io.vertx.lang.scala.ScalaVerticle
import io.vertx.scala.core.eventbus.Message

import scala.concurrent.{Future, Promise}

class DemoSender extends ScalaVerticle {

  override def startFuture(): Future[Unit] = {
    println("starting Demo Sender")
    val eb = vertx.eventBus()

    eb.publish("avengers.helicarrier.communication", "A genius billionaire, playboy, philanthropist…")

    Promise[Unit]().future
  }
}
