package demo

import io.vertx.lang.scala.ScalaVerticle

import scala.util.Random

class IronMan extends ScalaVerticle {

  override def start(): Unit = {

    val eb = vertx.eventBus()
    val quotes = Set("A genius billionaire, playboy, philanthropist…",
      "I'm sorry. Earth is closed today. So pack it up, and get out of here."
    )

    vertx.setPeriodic(5000, (id: Long) => {
      val quote = quotes.toVector(Random.nextInt(quotes.size))
      eb.publish("avengers.helicarrier.communication", quote)
    })

  }

}
