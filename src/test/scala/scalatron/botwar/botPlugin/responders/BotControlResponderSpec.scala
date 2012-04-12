package scalatron.botwar.botPlugin.responders

import org.specs2.Specification
import com.typesafe.config.ConfigFactory
import scala.collection.JavaConverters._
import scalatron.botwar.botPlugin.protocol._
import scalatron.botwar.botPlugin.strategies.StrategyChain
import scalatron.botwar.botPlugin.configuration.{ConfigBuilder, BotConfig}

class BotControlResponderSpec extends Specification with StrategyChain {def is =

  "Specification for BotControlResponder"                            ^
                                                                     endp^
  "A BotControlResponder should"                                     ^
    "generate a response string when there are no actions"           ! emptyResponse^
    "generate a response string from the contained actions"          ! actionsResponse^
    "not propagate actions from a previous reaction"                 ! noActionPropagation^
    "not react to a Welcome command"                                 ! welcomeCommand^
    "not react to a Goodbye command"                                 ! goodbyeCommand^
                                                                     endp^
  "Reacting to a React command on Master should return a responder that" ^
    "contains a simple action"                                       ! masterSimpleAction^
    "contains simple actions from multiple strategy groups"          ! masterMultipleActions^
    "removes tracked state if no update outome"                      ! masterRemoveTrackedState^
    "updates the tracked state for the master"                       ! masterUpdateTrackedState^
    "spawns a new mini bot with no state"                            ! masterSpawnWithNoState^
    "spawns a new mini bot with running state"                       ! masterSpawnWithRunningState^
    "spawns a new mini bot with tracked state"                       ! masterSpawnWithTrackedState^
                                                                     endp^
  "Reacting to a React command on MiniBot should return responder that" ^
    "contains a simple action"                                       ! miniBotSimpleAction^
    "contains simple actions from multiple strategy groups"          ! miniBotMultipleActions^
    "removed tracked stats if no update outcome"                     ! miniBotRemoveTrackedState^
    "updates the tracked state for the mini bot"                     ! miniBotUpdateTrackedState^
    "updates the name of the mini bot with running state"            ! miniBotUpdateRunningState^
                                                                     end

  def emptyResponse = {
    val config = ConfigBuilder(Vector())
    val environment = BotEnvironment(BotConfig(5000, 1, config), Vector.empty)
    val responder = BotControlResponder(environment)

    responder.response must_== ""
  }

  def actionsResponse = {
    val config = ConfigBuilder(Vector())
    val environment = BotEnvironment(BotConfig(5000, 1, config), Vector.empty)
    val responder = BotControlResponder(environment, Vector(Move(1, -1), Say("Test")))

    responder.response must_== "Move(dx=1,dy=-1)|Say(text=Test)"
  }

  def noActionPropagation = {
    val config = ConfigBuilder(Vector())
    val environment = BotEnvironment(BotConfig(5000, 1, config), Vector.empty)
    val responder = BotControlResponder(environment, Vector(Move(1, -1), Say("Test")))

    val result = responder.respond("React(entity=Master,time=1,view=____M____,energy=1000)").asInstanceOf[BotControlResponder]
    result.lastActions must beEmpty
  }

  def welcomeCommand = {
    val config = ConfigBuilder(Vector())
    val environment = BotEnvironment(BotConfig(5000, 1, config), Vector.empty)
    val responder = BotControlResponder(environment, Vector(Move(1, -1), Say("Test")))

    responder.respond("Welcome(name=Test,path=src/test/resources,apocalypse=5000,round=1)") must_== responder
  }

  def goodbyeCommand = {
    val config = ConfigBuilder(Vector())
    val environment = BotEnvironment(BotConfig(5000, 1, config), Vector.empty)
    val responder = BotControlResponder(environment, Vector(Move(1, -1), Say("Test")))

    responder.respond("Goodbye(energy=1000)") must_== responder
  }

  def masterSimpleAction = {
    val config = ConfigBuilder(Vector("movement" -> ("AlwaysMoveStrategy" :: Nil)))
    val botConfig = BotConfig(5000, 1, config)
    val environment = BotEnvironment(botConfig, createStrategyGroups(botConfig))
    val responder = BotControlResponder(environment).respond("React(entity=Master,time=1,view=____M____,energy=1000)")

    responder.asInstanceOf[BotControlResponder].lastActions must_== Seq(Move(1, -1))
  }

  def masterMultipleActions = {
    val config = ConfigBuilder(Vector("movement" -> ("AlwaysMoveStrategy" :: Nil), "debug" -> ("SayHelloStrategy" :: Nil)))
    val botConfig = BotConfig(5000, 1, config)
    val environment = BotEnvironment(botConfig, createStrategyGroups(botConfig))
    val responder = BotControlResponder(environment).respond("React(entity=Master,time=1,view=____M____,energy=1000)")

    responder.asInstanceOf[BotControlResponder].lastActions must_== Seq(Move(1, -1), Say("Hello"))
  }

  def masterRemoveTrackedState = {
    val config = ConfigBuilder(Vector("movement" -> ("AlwaysMoveStrategy" :: Nil)))
    val botConfig = BotConfig(5000, 1, config)
    val trackedState = Map("Master" -> Map("foo" -> "FOO"), "1" -> Map("bar" -> "BAR"))
    val environment = BotEnvironment(botConfig, createStrategyGroups(botConfig), trackedState)
    val responder = BotControlResponder(environment).respond("React(entity=Master,time=1,view=____M____,energy=1000)")

    responder.asInstanceOf[BotControlResponder].environment.trackedState must_== Map("1" -> Map("bar" -> "BAR"))
  }

  def masterUpdateTrackedState = {
    val config = ConfigBuilder(Vector("state" -> ("ReverseTrackedStateStrategy" :: Nil)))
    val botConfig = BotConfig(5000, 1, config)
    val trackedState = Map("Master" -> Map("foo" -> "FOO"))
    val environment = BotEnvironment(botConfig, createStrategyGroups(botConfig), trackedState)
    val responder = BotControlResponder(environment).respond("React(entity=Master,time=1,view=____M____,energy=1000)")

    responder.asInstanceOf[BotControlResponder].environment.trackedState must_== Map("Master" -> Map("foo" -> "OOF"))
  }

  def masterSpawnWithNoState = {
    val config = ConfigBuilder(Vector("state" -> ("SpawnWithNoStateStrategy" :: Nil)))
    val botConfig = BotConfig(5000, 1, config)
    val trackedState = Map("Master" -> Map("foo" -> "FOO"))
    val environment = BotEnvironment(botConfig, createStrategyGroups(botConfig), trackedState)
    val responder = BotControlResponder(environment).respond("React(entity=Master,time=1,view=____M____,energy=1000)").asInstanceOf[BotControlResponder]

    (responder.lastActions must_== Seq(Spawn(1, -1, "1:", 200))) and
    (responder.environment.sequenceGenerator.head must_== 2) and
    (responder.environment.trackedState.contains("1:") must beFalse)
  }

  def masterSpawnWithRunningState = {
    val config = ConfigBuilder(Vector("state" -> ("SpawnWithStateStrategy" :: Nil)))
    val botConfig = BotConfig(5000, 1, config)
    val trackedState = Map("Master" -> Map("foo" -> "FOO"))
    val environment = BotEnvironment(botConfig, createStrategyGroups(botConfig), trackedState)
    val responder = BotControlResponder(environment).respond("React(entity=Master,time=1,view=____M____,energy=1000)").asInstanceOf[BotControlResponder]

    responder.lastActions must_== Seq(Spawn(1, -1, "1:bar/BAR", 200))
  }

  def masterSpawnWithTrackedState = {
    val config = ConfigBuilder(Vector("state" -> ("SpawnWithStateStrategy" :: Nil)))
    val botConfig = BotConfig(5000, 1, config)
    val trackedState = Map("Master" -> Map("foo" -> "FOO"))
    val environment = BotEnvironment(botConfig, createStrategyGroups(botConfig), trackedState)
    val responder = BotControlResponder(environment).respond("React(entity=Master,time=1,view=____M____,energy=1000)").asInstanceOf[BotControlResponder]

    responder.environment.trackedState("1") must_== Map("baz" ->"BAZ")
  }

  def miniBotSimpleAction = {
    val config = ConfigBuilder(Vector("movement" -> ("AlwaysMoveStrategy" :: Nil)))
    val botConfig = BotConfig(5000, 1, config)
    val environment = BotEnvironment(botConfig, createStrategyGroups(botConfig))
    val responder = BotControlResponder(environment).respond("React(entity=1:,time=1,view=____M____,energy=1000,dx=10,dy=-10)")

    responder.asInstanceOf[BotControlResponder].lastActions must_== Seq(Move(1, -1))
  }

  def miniBotMultipleActions = {
    val config = ConfigBuilder(Vector("movement" -> ("AlwaysMoveStrategy" :: Nil), "debug" -> ("SayHelloStrategy" :: Nil)))
    val botConfig = BotConfig(5000, 1, config)
    val environment = BotEnvironment(botConfig, createStrategyGroups(botConfig))
    val responder = BotControlResponder(environment).respond("React(entity=1:,time=1,view=____M____,energy=1000,dx=10,dy=-10)")

    responder.asInstanceOf[BotControlResponder].lastActions must_== Seq(Move(1, -1), Say("Hello"))
  }

  def miniBotRemoveTrackedState = {
    val config = ConfigBuilder(Vector("movement" -> ("AlwaysMoveStrategy" :: Nil)))
    val botConfig = BotConfig(5000, 1, config)
    val trackedState = Map("Master" -> Map("foo" -> "FOO"), "1" -> Map("bar" -> "BAR"))
    val environment = BotEnvironment(botConfig, createStrategyGroups(botConfig), trackedState)
    val responder = BotControlResponder(environment).respond("React(entity=1:baz/BAZ,time=1,view=____M____,energy=1000,dx=10,dy=-10)")

    responder.asInstanceOf[BotControlResponder].environment.trackedState must_== Map("Master" -> Map("foo" -> "FOO"))
  }

  def miniBotUpdateTrackedState = {
    val config = ConfigBuilder(Vector("state" -> ("ReverseTrackedStateStrategy" :: Nil)))
    val botConfig = BotConfig(5000, 1, config)
    val trackedState = Map("Master" -> Map("foo" -> "FOO"), "1" -> Map("bar" -> "BAR"))
    val environment = BotEnvironment(botConfig, createStrategyGroups(botConfig), trackedState)
    val responder = BotControlResponder(environment).respond("React(entity=1:baz/BAZ,time=1,view=____M____,energy=1000,dx=10,dy=-10)")

    responder.asInstanceOf[BotControlResponder].environment.trackedState must_== Map("Master" -> Map("foo" -> "FOO"),
                                                                                     "1" -> Map("bar" -> "RAB"))
  }

  def miniBotUpdateRunningState = {
    val config = ConfigBuilder(Vector("state" -> ("ReverseRunningStateStrategy" :: Nil)))
    val botConfig = BotConfig(5000, 1, config)
    val trackedState = Map("1" -> Map("bar" -> "BAR"))
    val environment = BotEnvironment(botConfig, createStrategyGroups(botConfig), trackedState)
    val responder = BotControlResponder(environment).respond("React(entity=1:baz/BAZ,time=1,view=____M____,energy=1000,dx=10,dy=-10)")

    responder.asInstanceOf[BotControlResponder].lastActions must_== Seq(SetName("1:baz/ZAB"))
  }
}

