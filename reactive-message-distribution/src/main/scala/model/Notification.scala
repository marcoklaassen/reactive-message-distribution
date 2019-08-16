package model

import io.vertx.lang.scala.json.JsonObject
import model.Notification._

case class Notification(senderApplication: String, receiverIdentifier: String, message: String) {

  def toJson: JsonObject = {
    val json = new JsonObject()
    json.put(senderKey, senderApplication)
    json.put(receiverKey, receiverIdentifier)
    json.put(messageKey, message)
  }
}

object Notification {
  val senderKey = "senderApp"
  val receiverKey = "receiver"
  val messageKey = "message"

  def fromJson(jsonObject: JsonObject): Notification =
    Notification(
      jsonObject.getString(senderKey),
      jsonObject.getString(receiverKey),
      jsonObject.getString(messageKey)
    )
}
