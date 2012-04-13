package scalatron.botwar.toolkit.strategies

import scalatron.botwar.toolkit.configuration.BotConfig
import scalatron.botwar.toolkit.domain.{Request, Outcome}

trait Strategy {
  type StrategyFunction = PartialFunction[Request, Set[Outcome]]
  def react(config: BotConfig): StrategyFunction
}
