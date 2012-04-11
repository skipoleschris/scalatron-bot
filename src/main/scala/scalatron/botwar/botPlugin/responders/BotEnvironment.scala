package scalatron.botwar.botPlugin.responders

import scalatron.botwar.botPlugin.configuration.BotConfig
import scalatron.botwar.botPlugin.strategies.Strategy

case class BotEnvironment(botConfig: BotConfig,
                          strategies: Set[List[Strategy#StrategyFunction]],
                          trackedState: Map[String, Map[String, String]] = Map.empty,
                          sequenceGenerator: Stream[Int] = Stream.from(1)) {
  def updateTrackedState(name: String, entries: Map[String, String]) =
    copy(trackedState = if ( entries.isEmpty ) trackedState - name else trackedState.updated(name, entries))

  def replace(seq: Stream[Int]) = copy(sequenceGenerator = seq)
}
