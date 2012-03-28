package scalatron.botwar.botPlugin.strategies

import scalatron.botwar.botPlugin.protocol.{Command, Action}
import scalatron.botwar.botPlugin.configuration.BotConfig

trait Strategy {
  def react(config: BotConfig): PartialFunction[Command, IndexedSeq[Action]]
}
