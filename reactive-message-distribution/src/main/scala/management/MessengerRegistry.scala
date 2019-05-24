package management

import io.vertx.lang.scala.ScalaVerticle
import io.vertx.scala.core.eventbus.Message

class MessengerRegistry extends ScalaVerticle {

  override def start(): Unit = {

    var registeredMessengers = List[String]()

    val eb = vertx.eventBus()
    eb.consumer("avengers.helicarrier.registry", (message: Message[String]) => {
      registeredMessengers = registeredMessengers :+ message.body()
      println(s"[Messenger Registry] {${message.body()}} has registered")
    })

    vertx.setPeriodic(5000, (_: Long) => {
      println(s"[Messenger Registry] registered messengers: ${registeredMessengers}")
    })

  }

}
