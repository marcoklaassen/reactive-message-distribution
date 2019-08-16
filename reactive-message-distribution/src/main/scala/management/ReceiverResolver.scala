package management

import io.vertx.core.http.HttpMethod
import io.vertx.lang.scala.ScalaVerticle
import io.vertx.scala.ext.web.Router
import io.vertx.scala.ext.web.handler.BodyHandler


class ReceiverResolver extends ScalaVerticle {

  override def start(): Unit = {

    val eb = vertx.eventBus()

    val server = vertx.createHttpServer()
    val router = Router.router(vertx)

    router.route.handler(BodyHandler.create())

    router.route
      .consumes("*/json")
      .path("/contact/")
      .method(HttpMethod.POST)
      // TODO extract handler
      .handler((routingContext: io.vertx.scala.ext.web.RoutingContext) => {
        // add contact to addressBook
        routingContext.getBodyAsJson() match {
          case Some(contact) =>
            eb.publish("avengers.helicarrier.addressbook", contact)
            routingContext.response.end(contact.toString)
          case None => routingContext.response.setStatusCode(400).end("Your request contains no contact")
        }
      })

    router.route
      .consumes("*/json")
      .path("/config/")
      .method(HttpMethod.POST)
      // TODO extract handler
      .handler((routingContext: io.vertx.scala.ext.web.RoutingContext) => {
      // add contact to addressBook
      routingContext.getBodyAsJson() match {
        case Some(configuration) =>
          eb.publish("avengers.helicarrier.userconfig", configuration)
          routingContext.response.end(configuration.toString)
        case None => routingContext.response.setStatusCode(400).end("Your request contains no user configuration")
      }
    })

    server.requestHandler(router).listen(8080)
  }
}
