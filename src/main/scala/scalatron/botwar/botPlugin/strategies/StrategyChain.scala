package scalatron.botwar.botPlugin.strategies

import scalatron.botwar.botPlugin.domain.Request
import scalatron.botwar.botPlugin.configuration.BotConfig

trait StrategyChain {

  def forRequest(strategies: List[Strategy#StrategyFunction], request: Request): Option[Strategy#StrategyFunction] =
    strategies find (_.isDefinedAt(request))

  def createStrategyGroups(botConfig: BotConfig): Set[List[Strategy#StrategyFunction]] =
    (botConfig.strategyGroups map (_._2 map instantiate(botConfig))).toSet

  private def instantiate(botConfig: BotConfig)(strategyName: String) = try {
    val className = "scalatron.botwar.botPlugin.strategies."  + strategyName
    val clazz = getClass.getClassLoader.loadClass(className)
    val strategy = clazz.newInstance().asInstanceOf[Strategy]
    strategy.react(botConfig)
  } catch {
    case e => new NoOpStrategy().react(botConfig)
  }
}
