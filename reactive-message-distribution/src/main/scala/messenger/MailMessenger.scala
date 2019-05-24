package messenger

import io.vertx.core.{AsyncResult, Vertx}
import io.vertx.ext.mail._
import io.vertx.lang.scala.ScalaVerticle
import io.vertx.scala.core.eventbus.Message

class MailMessenger extends ScalaVerticle {

  override def start(): Unit = {
    val config = new MailConfig()

    // TODO beautify code structure
    config.setHostname(sys.env("SMTP-SERVER-HOST"))
    config.setPort(sys.env("SMTP-SERVER-PORT").toInt)
    config.setUsername(sys.env("SMTP-SERVER-USER"))
    config.setPassword(sys.env("SMTP-SERVER-PASS"))
    val mailClient = MailClient.createNonShared(Vertx.vertx, config)

    val eb = vertx.eventBus()

    eb.publish("avengers.helicarrier.registry", this.getClass.getName)

    eb.consumer("avengers.helicarrier.communication", (message: Message[String]) => {
      val mail = new MailMessage()
      mail.setFrom("tony.stark@avengers.org (Tony Stark)")
      mail.setTo("tony.stark@avengers.org")
      mail.setText(s"${message.body()}")

      mailClient.sendMail(mail, (result: AsyncResult[MailResult]) => {
        if (result.succeeded()) {
          println("[Mail Messenger] mail delivered")
        } else {
          println(result.cause.printStackTrace)
        }
      })
      println(s"[Mail Messenger] received a message: ${message.body()}")
    })
  }

}
