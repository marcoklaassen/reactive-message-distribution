package demo

import io.vertx.lang.scala.ScalaVerticle
import io.vertx.scala.core.Vertx
import management.MessengerRegistry
import messenger.{MailMessenger, TelegramMessenger}

import scala.concurrent.ExecutionContext
import scala.util.{Failure, Success}

object Main extends App {
  implicit val ec = ExecutionContext.global

  val vertx = Vertx.vertx
  vertx.deployVerticleFuture(ScalaVerticle.nameForVerticle[MessengerRegistry]).onComplete {

    case Success(_) => {
      vertx.deployVerticle(ScalaVerticle.nameForVerticle[MailMessenger])
      vertx.deployVerticle(ScalaVerticle.nameForVerticle[TelegramMessenger])
      vertx.deployVerticle(ScalaVerticle.nameForVerticle[IronMan])
    }
    case Failure(cause) => {
      println(s"$cause")
    }

  }

}
