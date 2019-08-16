package management

import io.vertx.core.json.JsonArray
import io.vertx.lang.scala.ScalaVerticle
import io.vertx.lang.scala.json.JsonObject
import io.vertx.scala.core.eventbus.Message
import model.Notification

import scala.collection.JavaConverters._

class NotificationManager extends ScalaVerticle {

  case class Contact(userId: String, messengerAddresses: Map[String, String])
  case class UserConfig(userId: String, apps: Set[AppConfig])
  case class AppConfig(appId: String, activationConfig: Map[String, Boolean])

  override def start(): Unit = {

    val registeredMessengers = new JsonArray()
    var userConfigs = List[UserConfig]()
    var addressBook = List[Contact]()

    val eb = vertx.eventBus()
    eb.consumer("avengers.helicarrier.messengers", (message: Message[String]) => {
      // update my messengers list
      registeredMessengers.add(message.body)
      println(s"[model.Notification Manager] registered messengers: $registeredMessengers")
    })

    eb.consumer("avengers.helicarrier.addressbook", (message: Message[JsonObject]) => {
      // update my address book

      val addressbookJson = message.body
      val userId = addressbookJson.getString("userId")

      val messengerAddresses = addressbookJson.fieldNames.asScala
        .filter(field => field != "userId")
        .map(messenger => messenger -> addressbookJson.getString(messenger))
        .toMap

      addressBook = addressBook :+ Contact(userId, messengerAddresses)

      println(s"[model.Notification Manager] registered contacts: $addressBook")
    })

    eb.consumer("avengers.helicarrier.communication", (message: Message[JsonObject]) => {
      // find all relevant messengers and publish message on messenger-specific channel
      val notification = Notification.fromJson(message.body)

      // get user-config for current receiver
      val maybeUserConfig = userConfigs
        .find(userConfig => userConfig.userId == notification.receiverIdentifier)

      // get the app-config for the current sender-application
      val maybeAppConfig = maybeUserConfig
        .flatMap(receiverConfig => receiverConfig.apps.find(app => app.appId == notification.senderApplication))

      // get the activated messengers for the current sender-application of our receiver
      val mayBeActivatedConfig = maybeAppConfig
        .map(appConfig => appConfig.activationConfig.filter(_._2))

      // publish message for all activated messengers
      for ((app, _) <- mayBeActivatedConfig.getOrElse(Map.empty[String, Boolean])) eb.publish(s"avengers.helicarrier.communication.$app", notification.message)

      println(s"[model.Notification Manager] received Message: ${message.body}")
    })

    eb.consumer("avengers.helicarrier.userconfig", (message: Message[JsonObject]) => {
      // update user configs
      val userConfigJson = message.body
      val userId = userConfigJson.getString("userId")

      val appConfigJson = userConfigJson.getJsonObject("appConfig")
      val appConfigs = appConfigJson.fieldNames.asScala.map(app => {
        val appConfig = appConfigJson.getJsonObject(app)
        val activationMap = appConfig.fieldNames.asScala.map(messenger => messenger -> appConfig.getBoolean(messenger).booleanValue).toMap
        AppConfig(app, activationMap)
      })

      userConfigs = userConfigs :+ UserConfig(userId, Set.empty ++ appConfigs)

      println(s"[model.Notification Manager] user configurations: $userConfigs")
    })

  }

}
