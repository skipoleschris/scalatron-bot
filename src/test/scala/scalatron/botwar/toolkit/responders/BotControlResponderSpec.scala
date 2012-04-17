package scalatron.botwar.toolkit.responders

import org.specs2.Specification
import scalatron.botwar.toolkit.protocol._
import scalatron.botwar.toolkit.strategies.StrategyChain
import scalatron.botwar.toolkit.configuration.{ConfigBuilder, BotConfig}

class BotControlResponderSpec extends Specification with StrategyChain {def is =

  "Specification for BotControlResponder"                            ^
                                                                     endp^
  "A BotControlResponder should"                                     ^
    "not react to a Welcome command"                                 ! welcomeCommand^
    "not react to a Goodbye command"                                 ! goodbyeCommand^
                                                                     endp^
  "Reacting to a React command on Master should return a response that" ^
    "contains a simple action"                                       ! masterSimpleAction^
    "contains simple actions from multiple strategy groups"          ! masterMultipleActions^
    "removes tracked state if no update outome"                      ! masterRemoveTrackedState^
    "updates the tracked state for the master"                       ! masterUpdateTrackedState^
    "spawns a new mini bot with no state"                            ! masterSpawnWithNoState^
    "spawns a new mini bot with running state"                       ! masterSpawnWithRunningState^
    "spawns a new mini bot with tracked state"                       ! masterSpawnWithTrackedState^
                                                                     endp^
  "Reacting to a React command on MiniBot should return response that" ^
    "contains a simple action"                                       ! miniBotSimpleAction^
    "contains simple actions from multiple strategy groups"          ! miniBotMultipleActions^
    "removed tracked stats if no update outcome"                     ! miniBotRemoveTrackedState^
    "updates the tracked state for the mini bot"                     ! miniBotUpdateTrackedState^
    "updates the name of the mini bot with running state"            ! miniBotUpdateRunningState^
                                                                     end

  def welcomeCommand = {
    val config = ConfigBuilder(Vector())
    val context = TestBotContext(BotConfig(5000, 1, config), Vector.empty)
    val responder = BotControlResponder(context, Stream.from(1))

    responder.respond("Welcome(name=Test,path=src/test/resources,apocalypse=5000,round=1)") must_== Response.empty
  }

  def goodbyeCommand = {
    val config = ConfigBuilder(Vector())
    val context = TestBotContext(BotConfig(5000, 1, config), Vector.empty)
    val responder = BotControlResponder(context, Stream.from(1))

    responder.respond("Goodbye(energy=1000)") must_== Response.empty
  }

  def masterSimpleAction = {
    val config = ConfigBuilder(Vector("movement" -> ("AlwaysMoveStrategy" :: Nil)))
    val botConfig = BotConfig(5000, 1, config)
    val context = TestBotContext(botConfig, createStrategyGroups(botConfig))
    val response = BotControlResponder(context, Stream.from(1)).respond("React(entity=Master,time=1,view=____M____,energy=1000)")

    response.actions must_== IndexedSeq(Move(1, -1))
  }

  def masterMultipleActions = {
    val config = ConfigBuilder(Vector("movement" -> ("AlwaysMoveStrategy" :: Nil), "debug" -> ("SayHelloStrategy" :: Nil)))
    val botConfig = BotConfig(5000, 1, config)
    val context = TestBotContext(botConfig, createStrategyGroups(botConfig))
    val response = BotControlResponder(context, Stream.from(1)).respond("React(entity=Master,time=1,view=____M____,energy=1000)")

    response.actions must_== IndexedSeq(Move(1, -1), Say("Hello"))
  }

  def masterRemoveTrackedState = {
    val config = ConfigBuilder(Vector("movement" -> ("AlwaysMoveStrategy" :: Nil)))
    val botConfig = BotConfig(5000, 1, config)
    val trackedState = Map("Master" -> Map("foo" -> "FOO"), "1" -> Map("bar" -> "BAR"))
    val context = TestBotContext(botConfig, createStrategyGroups(botConfig), trackedState)
    val response = BotControlResponder(context, Stream.from(1)).respond("React(entity=Master,time=1,view=____M____,energy=1000)")

    response.trackedState must_== Map("Master" -> Map.empty)
  }

  def masterUpdateTrackedState = {
    val config = ConfigBuilder(Vector("state" -> ("ReverseTrackedStateStrategy" :: Nil)))
    val botConfig = BotConfig(5000, 1, config)
    val trackedState = Map("Master" -> Map("foo" -> "FOO"))
    val context = TestBotContext(botConfig, createStrategyGroups(botConfig), trackedState)
    val response = BotControlResponder(context, Stream.from(1)).respond("React(entity=Master,time=1,view=____M____,energy=1000)")

    response.trackedState must_== Map("Master" -> Map("foo" -> "OOF"))
  }

  def masterSpawnWithNoState = {
    val config = ConfigBuilder(Vector("state" -> ("SpawnWithNoStateStrategy" :: Nil)))
    val botConfig = BotConfig(5000, 1, config)
    val trackedState = Map("Master" -> Map("foo" -> "FOO"))
    val context = TestBotContext(botConfig, createStrategyGroups(botConfig), trackedState)
    val response = BotControlResponder(context, Stream.from(1)).respond("React(entity=Master,time=1,view=____M____,energy=1000)")

    (response.actions must_== IndexedSeq(Spawn(1, -1, "1:", 200))) and
    (response.trackedState must_== Map("Master" -> Map.empty, "1" -> Map.empty))
  }

  def masterSpawnWithRunningState = {
    val config = ConfigBuilder(Vector("state" -> ("SpawnWithStateStrategy" :: Nil)))
    val botConfig = BotConfig(5000, 1, config)
    val trackedState = Map("Master" -> Map("foo" -> "FOO"))
    val context = TestBotContext(botConfig, createStrategyGroups(botConfig), trackedState)
    val response = BotControlResponder(context, Stream.from(1)).respond("React(entity=Master,time=1,view=____M____,energy=1000)")

    response.actions must_== IndexedSeq(Spawn(1, -1, "1:bar/BAR", 200))
  }

  def masterSpawnWithTrackedState = {
    val config = ConfigBuilder(Vector("state" -> ("SpawnWithStateStrategy" :: Nil)))
    val botConfig = BotConfig(5000, 1, config)
    val trackedState = Map("Master" -> Map("foo" -> "FOO"))
    val context = TestBotContext(botConfig, createStrategyGroups(botConfig), trackedState)
    val response = BotControlResponder(context, Stream.from(1)).respond("React(entity=Master,time=1,view=____M____,energy=1000)")

    response.trackedState must_== Map("Master" -> Map(), "1" -> Map("baz" ->"BAZ"))
  }

  def miniBotSimpleAction = {
    val config = ConfigBuilder(Vector("movement" -> ("AlwaysMoveStrategy" :: Nil)))
    val botConfig = BotConfig(5000, 1, config)
    val context = TestBotContext(botConfig, createStrategyGroups(botConfig))
    val response = BotControlResponder(context, Stream.from(1)).respond("React(entity=1:,time=1,view=____M____,energy=1000,dx=10,dy=-10)")

    response.actions must_== IndexedSeq(Move(1, -1))
  }

  def miniBotMultipleActions = {
    val config = ConfigBuilder(Vector("movement" -> ("AlwaysMoveStrategy" :: Nil), "debug" -> ("SayHelloStrategy" :: Nil)))
    val botConfig = BotConfig(5000, 1, config)
    val context = TestBotContext(botConfig, createStrategyGroups(botConfig))
    val response = BotControlResponder(context, Stream.from(1)).respond("React(entity=1:,time=1,view=____M____,energy=1000,dx=10,dy=-10)")

    response.actions must_== IndexedSeq(Move(1, -1), Say("Hello"))
  }

  def miniBotRemoveTrackedState = {
    val config = ConfigBuilder(Vector("movement" -> ("AlwaysMoveStrategy" :: Nil)))
    val botConfig = BotConfig(5000, 1, config)
    val trackedState = Map("Master" -> Map("foo" -> "FOO"), "1" -> Map("bar" -> "BAR"))
    val context = TestBotContext(botConfig, createStrategyGroups(botConfig), trackedState)
    val response = BotControlResponder(context, Stream.from(1)).respond("React(entity=1:baz/BAZ,time=1,view=____M____,energy=1000,dx=10,dy=-10)")

    response.trackedState must_== Map("1" -> Map.empty)
  }

  def miniBotUpdateTrackedState = {
    val config = ConfigBuilder(Vector("state" -> ("ReverseTrackedStateStrategy" :: Nil)))
    val botConfig = BotConfig(5000, 1, config)
    val trackedState = Map("Master" -> Map("foo" -> "FOO"), "1" -> Map("bar" -> "BAR"))
    val context = TestBotContext(botConfig, createStrategyGroups(botConfig), trackedState)
    val response = BotControlResponder(context, Stream.from(1)).respond("React(entity=1:baz/BAZ,time=1,view=____M____,energy=1000,dx=10,dy=-10)")

    response.trackedState must_== Map("1" -> Map("bar" -> "RAB"))
  }

  def miniBotUpdateRunningState = {
    val config = ConfigBuilder(Vector("state" -> ("ReverseRunningStateStrategy" :: Nil)))
    val botConfig = BotConfig(5000, 1, config)
    val trackedState = Map("1" -> Map("bar" -> "BAR"))
    val context = TestBotContext(botConfig, createStrategyGroups(botConfig), trackedState)
    val response = BotControlResponder(context, Stream.from(1)).respond("React(entity=1:baz/BAZ,time=1,view=____M____,energy=1000,dx=10,dy=-10)")

    response.actions must_== IndexedSeq(SetName("1:baz/ZAB"))
  }
}

