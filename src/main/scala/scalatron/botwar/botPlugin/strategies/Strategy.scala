package scalatron.botwar.botPlugin.strategies

import scalatron.botwar.botPlugin.configuration.BotConfig
import scalatron.botwar.botPlugin.domain.{Request, Outcome}

trait Strategy {
  type StrategyFunction = PartialFunction[Request, Set[Outcome]]
  def react(config: BotConfig): StrategyFunction
}
