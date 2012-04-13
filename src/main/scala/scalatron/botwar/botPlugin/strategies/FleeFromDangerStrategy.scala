package scalatron.botwar.botPlugin.strategies

import scalatron.botwar.botPlugin.configuration.BotConfig
import scalatron.botwar.botPlugin.domain.{Request, State, Context}
import scalatron.botwar.botPlugin.extractors.InDanger

class FleeFromDangerStrategy extends Strategy {
  def react(config: BotConfig) = {
    case Request(Context(_, _, _, InDanger(view), _), _) => {
      Set()
    }
  }
}
