package scalatron.botwar.botPlugin.responders

import org.specs2.Specification
import com.typesafe.config.ConfigFactory
import scala.collection.JavaConverters._
import scalatron.botwar.botPlugin.configuration.BotConfig
import scalatron.botwar.botPlugin.protocol.{Say, Move}

class BotControlResponderSpec extends Specification {def is =

  "Specification for BotControlResponder"                            ^
                                                                     endp^
  "A BotControlResponder should"                                     ^
    "generate a response string when there are no actions"           ! emptyResponse^
    "generate a response string from the contained actions"          ! actionsResponse^
    "not react to a Welcome command"                                 ! welcomeCommand^
    "not react to a Goodbye command"                                 ! goodbyeCommand^
                                                                     end

  def emptyResponse = {
    val configEntries = Map(("bot.strategies" -> (Nil).asJava)).asJava
    val config = ConfigFactory.parseMap(configEntries)

    val environment = BotEnvironment(BotConfig(5000, 1, config), Nil)
    val responder = BotControlResponder(environment)

    responder.response must_== ""
  }

  def actionsResponse = {
    val configEntries = Map(("bot.strategies" -> (Nil).asJava)).asJava
    val config = ConfigFactory.parseMap(configEntries)

    val environment = BotEnvironment(BotConfig(5000, 1, config), Nil)
    val responder = BotControlResponder(environment, Vector(Move(1, -1), Say("Test")))

    responder.response must_== "Move(dx=1,dy=-1)|Say(text=Test)"
  }

  def welcomeCommand = {
    val configEntries = Map(("bot.strategies" -> (Nil).asJava)).asJava
    val config = ConfigFactory.parseMap(configEntries)

    val environment = BotEnvironment(BotConfig(5000, 1, config), Nil)
    val responder = BotControlResponder(environment, Vector(Move(1, -1), Say("Test")))

    responder.respond("Welcome(name=Test,path=src/test/resources,apocalypse=5000,round=1)") must_== responder
  }

  def goodbyeCommand = {
    val configEntries = Map(("bot.strategies" -> (Nil).asJava)).asJava
    val config = ConfigFactory.parseMap(configEntries)

    val environment = BotEnvironment(BotConfig(5000, 1, config), Nil)
    val responder = BotControlResponder(environment, Vector(Move(1, -1), Say("Test")))

    responder.respond("Goodbye(energy=1000)") must_== responder
  }
}

