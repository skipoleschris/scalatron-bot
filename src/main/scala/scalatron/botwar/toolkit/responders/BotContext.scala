package scalatron.botwar.toolkit.responders

import scalatron.botwar.toolkit.configuration.BotConfig
import scalatron.botwar.toolkit.strategies.StrategyChain

trait BotContext {
  def initialised: Boolean
  def config: BotConfig
  def strategies: StrategyChain#StrategySeq
  def trackedState(botName: String): Map[String, String]
}


