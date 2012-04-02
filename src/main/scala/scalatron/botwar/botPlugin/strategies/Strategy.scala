package scalatron.botwar.botPlugin.strategies

import scalatron.botwar.botPlugin.configuration.BotConfig
import scalatron.botwar.botPlugin.domain.{Request, Outcome}

trait Strategy {
  def react(config: BotConfig): PartialFunction[Request, Set[Outcome]]
}
