package scalatron.botwar.botPlugin.strategies

import scalatron.botwar.botPlugin.configuration.BotConfig
import scalatron.botwar.botPlugin.protocol._
import scalatron.botwar.botPlugin.domain.{Context, Request}

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
