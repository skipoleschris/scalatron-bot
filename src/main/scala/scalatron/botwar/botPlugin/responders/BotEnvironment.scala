package scalatron.botwar.botPlugin.responders

import scalatron.botwar.botPlugin.configuration.BotConfig
import scalatron.botwar.botPlugin.strategies.StrategyChain

case class BotEnvironment(botConfig: BotConfig,
                          strategies: List[StrategyChain#StrategyFunction],
                          trackedState: Map[String, Map[String, String]] = Map()) {
  def updateTrackedState(name: String, entries: Map[String, String]) =
    copy(trackedState = trackedState.updated(name, entries))
}
