package scalatron.botwar.toolkit.responders

import scalatron.botwar.toolkit.configuration.BotConfig
import scalatron.botwar.toolkit.strategies.Strategy

case class BotEnvironment(botConfig: BotConfig,
                          strategies: IndexedSeq[List[Strategy#StrategyFunction]],
                          trackedState: Map[String, Map[String, String]] = Map.empty,
                          sequenceGenerator: Stream[Int] = Stream.from(1)) {
  def updateTrackedState(name: String, entries: Map[String, String]) =
    copy(trackedState = if ( entries.isEmpty ) trackedState - name else trackedState.updated(name, entries))

  def replace(seq: Stream[Int]) = copy(sequenceGenerator = seq)
}
