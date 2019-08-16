package management

import io.vertx.lang.scala.ScalaVerticle
import io.vertx.scala.core.eventbus.Message

class MessengerRegistry extends ScalaVerticle {

  override def start(): Unit = {

    val eb = vertx.eventBus()
    eb.consumer("avengers.helicarrier.registry", (message: Message[String]) => {
      eb.publish("avengers.helicarrier.messengers", message.body)
      println(s"[Messenger Registry] {${message.body()}} has registered")
    })

  }

}
