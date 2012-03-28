package scalatron.botwar.botPlugin.strategies

import scalatron.botwar.botPlugin.protocol._
import scalatron.botwar.botPlugin.configuration.BotConfig

trait StrategyChain {

  type StrategyFunction = PartialFunction[Command, IndexedSeq[Action]]
  private var strategies = List[StrategyFunction](new ConfigurationStrategy().react)

  def installStrategies(newStrategyNames: List[String], config: BotConfig): Unit =
    strategies = (newStrategyNames map instantiate(config)) ++ strategies

  def forCommand(command: Command): Option[StrategyFunction] = strategies find (_.isDefinedAt(command))

  private[strategies] def installedStrategies = strategies

  private def instantiate(config: BotConfig)(strategyName: String) = try {
    val className = "scalatron.botwar.botPlugin.strategies."  + strategyName
    val clazz = getClass.getClassLoader.loadClass(className)
    val strategy = clazz.newInstance().asInstanceOf[Strategy]
    strategy.react(config)
  } catch {
    case e => new NoOpStrategy().react(config)
  }
}
