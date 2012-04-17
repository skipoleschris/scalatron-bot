package scalatron.botwar.toolkit.responders

import scalatron.botwar.toolkit.configuration.BotConfig
import scalatron.botwar.toolkit.strategies.StrategyChain

case class TestBotContext(config: BotConfig = new BotConfig(5000, 1, null),
                          strategies: StrategyChain#StrategySeq = IndexedSeq(),
                          currentTrackedState: Map[String, Map[String,String]] = Map.empty,
                          initialised: Boolean = true) extends BotContext {
  def trackedState(botName: String) = currentTrackedState get botName getOrElse Map.empty
}
