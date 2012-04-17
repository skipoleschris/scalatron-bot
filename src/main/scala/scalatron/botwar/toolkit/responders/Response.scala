package scalatron.botwar.toolkit.responders

import scalatron.botwar.toolkit.protocol.Action
import scalatron.botwar.toolkit.configuration.BotConfig
import scalatron.botwar.toolkit.strategies.StrategyChain

case class Response(actions: IndexedSeq[Action] = Vector.empty,
                    trackedState: Map[String, Map[String, String]] = Map.empty,
                    environment: Option[(BotConfig, StrategyChain#StrategySeq)] = None) {
  def response = (actions map (_.toString)) mkString "|"

  def updateTrackedState(name: String, entries: Map[String, String]) =
    copy(trackedState = trackedState.updated(name, entries))

  def foreachTrackedState(f: (String, Map[String, String]) => Unit): Unit =
    trackedState foreach (entry => f(entry._1, entry._2))

  def forEnvironment(f: (BotConfig,  StrategyChain#StrategySeq) => Unit): Unit =
    environment foreach (entry => f(entry._1, entry._2))
}

object Response {
  lazy val empty = Response()
}
