package demo

import io.vertx.lang.scala.ScalaVerticle
import io.vertx.scala.core.Vertx
import management.{MessengerRegistry, NotificationManager, ReceiverResolver}
import messenger.{MailMessenger, TelegramMessenger}

import scala.concurrent.{ExecutionContext, ExecutionContextExecutor}
import scala.util.{Failure, Success}

object Main extends App {
  implicit val ec: ExecutionContextExecutor = ExecutionContext.global

  val vertx = Vertx.vertx
  vertx.deployVerticleFuture(ScalaVerticle.nameForVerticle[MessengerRegistry]).onComplete {

    case Success(_) =>
      vertx.deployVerticle(ScalaVerticle.nameForVerticle[MailMessenger])
      vertx.deployVerticle(ScalaVerticle.nameForVerticle[TelegramMessenger])
      vertx.deployVerticle(ScalaVerticle.nameForVerticle[IronMan])
      vertx.deployVerticle(ScalaVerticle.nameForVerticle[ReceiverResolver])
      vertx.deployVerticle(ScalaVerticle.nameForVerticle[NotificationManager])
    case Failure(cause) =>
      println(s"$cause")

  }
}
