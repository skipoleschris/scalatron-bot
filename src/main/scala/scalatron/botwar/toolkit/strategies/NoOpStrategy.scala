package scalatron.botwar.toolkit.strategies

import scalatron.botwar.toolkit.domain.Request
import scalatron.botwar.toolkit.configuration.BotConfig

class NoOpStrategy  extends Strategy {
  def react(config: BotConfig) = new StrategyFunction {
    def apply(command: Request) = throw new IllegalStateException()
    def isDefinedAt(command: Request) = false
  }
}
