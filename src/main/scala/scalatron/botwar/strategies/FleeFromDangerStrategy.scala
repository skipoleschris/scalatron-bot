package scalatron.botwar.strategies

import scalatron.botwar.toolkit.configuration.BotConfig
import scalatron.botwar.toolkit.domain.{Request, State, Context}
import scalatron.botwar.toolkit.extractors.InDanger
import scalatron.botwar.toolkit.strategies.Strategy

class FleeFromDangerStrategy extends Strategy {
  def react(config: BotConfig) = {
    case Request(Context(_, _, _, InDanger(view), _), _) => {
      Set()
    }
  }
}
