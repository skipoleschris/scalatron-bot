package scalatron.botwar.toolkit.strategies

import scalatron.botwar.toolkit.domain.Request
import scalatron.botwar.toolkit.configuration.BotConfig

trait StrategyChain {

  type StrategySeq = IndexedSeq[List[Strategy#StrategyFunction]]

  def forRequest(strategies: List[Strategy#StrategyFunction], request: Request): Option[Strategy#StrategyFunction] =
    strategies find (_.isDefinedAt(request))

  def createStrategyGroups(botConfig: BotConfig): StrategySeq =
    (botConfig.strategyGroups map (_._2 map instantiate(botConfig))).toIndexedSeq

  private def instantiate(botConfig: BotConfig)(strategyName: String) = try {
    val className = "scalatron.botwar.strategies."  + strategyName
    val clazz = getClass.getClassLoader.loadClass(className)
    val strategy = clazz.newInstance().asInstanceOf[Strategy]
    strategy.react(botConfig)
  } catch {
    case e => new NoOpStrategy().react(botConfig)
  }
}
