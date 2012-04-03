package scalatron.botwar.botPlugin.responders

import scalatron.botwar.botPlugin.configuration.Configuration
import scalatron.botwar.botPlugin.strategies.StrategyChain
import scalatron.botwar.botPlugin.protocol.{Welcome, Command}

case object InitialisingResponder extends BotResponder with Configuration with StrategyChain {
  def response = ""

  protected def forCommand(command: Command): Option[BotResponder] = command match {
    case Welcome(_, path, apocalypse, round) => {
      val botConfig = configure(path, apocalypse, round)
      val strategies = createStrategies(botConfig)
      Some(BotControlResponder(BotEnvironment(botConfig, strategies)))
    }
    case _ => None
  }
}
