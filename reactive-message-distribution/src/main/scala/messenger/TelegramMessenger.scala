package messenger

import io.vertx.lang.scala.ScalaVerticle
import io.vertx.scala.core.eventbus.Message

class TelegramMessenger extends ScalaVerticle {

  override def start(): Unit = {

    val eb = vertx.eventBus()
    eb.publish("avengers.helicarrier.registry", this.getClass.getName)

    eb.consumer("avengers.helicarrier.communication", (message: Message[String]) => {
      println(s"[Telegram Messenger] received a message: ${message.body()}")
    })

  }

}
