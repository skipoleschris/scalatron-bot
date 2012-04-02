package scalatron.botwar.botPlugin.strategies

import scalatron.botwar.botPlugin.configuration.Configuration
import scalatron.botwar.botPlugin.domain.{Request, Outcome}

trait StrategyChain {
  this: Configuration =>

  type StrategyFunction = PartialFunction[Request, Set[Outcome]]
  private var strategies = List[StrategyFunction]()

  def installStrategies(newStrategyNames: List[String]): Unit =
    strategies = (newStrategyNames map instantiate) ++ strategies

  def forRequest(request: Request): Option[StrategyFunction] = strategies find (_.isDefinedAt(request))

  private[strategies] def installedStrategies = strategies

  private def instantiate(strategyName: String) = try {
    val className = "scalatron.botwar.botPlugin.strategies."  + strategyName
    val clazz = getClass.getClassLoader.loadClass(className)
    val strategy = clazz.newInstance().asInstanceOf[Strategy]
    strategy.react(botConfig)
  } catch {
    case e => new NoOpStrategy().react(botConfig)
  }
}
