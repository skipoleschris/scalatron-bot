package scalatron.botwar.toolkit.strategies

import scalatron.botwar.toolkit.configuration.BotConfig
import scalatron.botwar.toolkit.protocol._
import scalatron.botwar.toolkit.domain._

class TestStrategy1 extends Strategy {
  def react(config: BotConfig) = {
    case Request(Context("Unknown", _, _, _, _),_) => null
  }
}

class TestStrategy2 extends Strategy {
  def react(config: BotConfig) = {
    case Request(Context("Master", _, _, _, _),_) => null
  }
}

class AlwaysMoveStrategy extends Strategy {
  def react(config: BotConfig) = {
    case Request(_, _) => Set(MoveOutcome(DeltaOffset(1, -1)))
  }
}

class SayHelloStrategy extends Strategy {
  def react(config: BotConfig) = {
    case Request(_, _) => Set(SayOutcome("Hello"))
  }
}

class ReverseTrackedStateStrategy extends Strategy {
  def react(config: BotConfig) = {
    case Request(_, State(_, tracked)) => Set(UpdateTrackedState(tracked map (entry => (entry._1, entry._2.reverse))))
  }
}

class SpawnWithNoStateStrategy extends Strategy {
  def react(config: BotConfig) = {
    case Request(Context("Master", _, _, _, _), _) => Set(SpawnOutcome(DeltaOffset(1, -1), 200, State(Map.empty, Map.empty)))
  }
}

class SpawnWithStateStrategy extends Strategy {
  def react(config: BotConfig) = {
    case Request(Context("Master", _, _, _, _), _) =>
      Set(SpawnOutcome(DeltaOffset(1, -1), 200, State(Map("bar" -> "BAR"), Map("baz" -> "BAZ"))))
  }
}

class ReverseRunningStateStrategy extends Strategy {
  def react(config: BotConfig) = {
    case Request(Context(name, _, _, _, _), State(running, _)) if ( name != "Master" ) =>
      Set(UpdateRunningState(name, running map (entry => (entry._1, entry._2.reverse))))
  }
}

class ExplodeStrategy extends Strategy {
  def react(config: BotConfig) = {
    case Request(Context(name, _, _, _, _), _) if ( name != "Master" ) => Set(ExplodeOutcome(3))
  }
}

