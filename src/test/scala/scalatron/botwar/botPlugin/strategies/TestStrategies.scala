package scalatron.botwar.botPlugin.strategies

import scalatron.botwar.botPlugin.configuration.BotConfig
import scalatron.botwar.botPlugin.protocol._

class TestStrategy1 extends Strategy {
  def react(config: BotConfig) = {
    case Welcome(_, _, _, _) => Vector[Action]()
  }
}

class TestStrategy2 extends Strategy {
  def react(config: BotConfig) = {
    case ReactBot("Master", _, _, _) => Vector[Action](Move(1, 0), Status("foo"))
  }
}

class TestStrategy3 extends Strategy {
  def react(config: BotConfig) = {
    case Goodbye(_) => Vector[Action]()
  }
}
