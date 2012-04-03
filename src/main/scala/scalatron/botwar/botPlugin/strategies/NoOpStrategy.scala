package scalatron.botwar.botPlugin.strategies

import scalatron.botwar.botPlugin.domain.Request
import scalatron.botwar.botPlugin.configuration.BotConfig

class NoOpStrategy  extends Strategy {
  def react(config: BotConfig) = new StrategyFunction {
    def apply(command: Request) = throw new IllegalStateException()
    def isDefinedAt(command: Request) = false
  }
}
