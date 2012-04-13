package scalatron.botwar.toolkit.responders

import scalatron.botwar.toolkit.configuration.Configuration
import scalatron.botwar.toolkit.strategies.StrategyChain
import scalatron.botwar.toolkit.protocol.{Welcome, Command}

case object InitialisingResponder extends BotResponder with Configuration with StrategyChain {
  def response = ""

  protected def forCommand(command: Command): Option[BotResponder] = command match {
    case Welcome(_, path, apocalypse, round) => {
      val botConfig = configure(path, apocalypse, round)
      val strategies = createStrategyGroups(botConfig)
      Some(BotControlResponder(BotEnvironment(botConfig, strategies)))
    }
    case _ => None
  }
}
