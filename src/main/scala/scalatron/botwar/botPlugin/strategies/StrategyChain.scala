package scalatron.botwar.botPlugin.strategies

import scalatron.botwar.botPlugin.protocol._

trait StrategyChain {

  type StrategyFunction = PartialFunction[Command, IndexedSeq[Action]]
  private var strategies = List[StrategyFunction]()
//  private var strategies = List[StrategyFunction](new RandomMovementStrategy().react)

  def installStrategies(newStrategies: List[StrategyFunction]): Unit =
    strategies = newStrategies ++ strategies

  def forCommand(command: Command): Option[StrategyFunction] = strategies find (_.isDefinedAt(command))

  private[strategies] def installedStrategies = strategies
}
