package demo

import io.vertx.lang.scala.ScalaVerticle
import model.Notification

import scala.util.Random

class IronMan extends ScalaVerticle {

  override def start(): Unit = {

    val eb = vertx.eventBus()
    val quotes = Set("A genius billionaire, playboy, philanthropist…",
      "I'm sorry. Earth is closed today. So pack it up, and get out of here."
    )

    val users = Set("IronMan", "CaptainAmerica")

    val apps = Set("app1", "app2")

    vertx.setPeriodic(5000, (_: Long) => {
      val quote = quotes.toVector(Random.nextInt(quotes.size))
      val app = apps.toVector(Random.nextInt(apps.size))
      val user = users.toVector(Random.nextInt(users.size))

      val message = Notification(app, user, quote)

      eb.publish("avengers.helicarrier.communication", message.toJson)
    })

  }

}
