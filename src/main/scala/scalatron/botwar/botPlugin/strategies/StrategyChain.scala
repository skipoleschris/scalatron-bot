package scalatron.botwar.botPlugin.strategies

import scalatron.botwar.botPlugin.domain.{Request, Outcome}
import scalatron.botwar.botPlugin.configuration.BotConfig

trait StrategyChain {
  type StrategyFunction = PartialFunction[Request, Set[Outcome]]

  def forRequest(strategies: List[StrategyFunction], request: Request): Option[StrategyFunction] =
    strategies find (_.isDefinedAt(request))

  def createStrategies(botConfig: BotConfig): List[StrategyFunction] = {
    val newStrategyNames = botConfig.strategyNames map (_.unwrapped().toString)
    newStrategyNames map instantiate(botConfig)
  }

  private def instantiate(botConfig: BotConfig)(strategyName: String) = try {
    val className = "scalatron.botwar.botPlugin.strategies."  + strategyName
    val clazz = getClass.getClassLoader.loadClass(className)
    val strategy = clazz.newInstance().asInstanceOf[Strategy]
    strategy.react(botConfig)
  } catch {
    case e => new NoOpStrategy().react(botConfig)
  }
}
