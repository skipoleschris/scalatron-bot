package scalatron.botwar.botPlugin.strategies

import scalatron.botwar.botPlugin.configuration.BotConfig
import scalatron.botwar.botPlugin.protocol.{Command, Action}

class NoOpStrategy  extends Strategy {
  def react(config: BotConfig) = new PartialFunction[Command, IndexedSeq[Action]] {
    def apply(v1: Command) = Vector[Action]()
    def isDefinedAt(command: Command) = false
  }
}
