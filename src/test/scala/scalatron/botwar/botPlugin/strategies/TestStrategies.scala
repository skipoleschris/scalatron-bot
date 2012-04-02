package scalatron.botwar.botPlugin.strategies

import scalatron.botwar.botPlugin.configuration.BotConfig
import scalatron.botwar.botPlugin.protocol._

class TestStrategy1 extends Strategy {
  def react = {
    case Welcome(_, _, _, _) => null
  }
}

class TestStrategy2 extends Strategy {
  def react = {
    case ReactBot("Master", _, _, _) => null
  }
}

class TestStrategy3 extends Strategy {
  def react = {
    case Goodbye(_) => null
  }
}
