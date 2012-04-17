package scalatron.botwar.toolkit.responders

import scalatron.botwar.toolkit.configuration.Configuration
import scalatron.botwar.toolkit.strategies.StrategyChain
import scalatron.botwar.toolkit.protocol.{Welcome, Command}

case class InitialisingResponder(context: BotContext) extends BotResponder with Configuration with StrategyChain {
  protected def forCommand(command: Command): Option[Response] = command match {
    case Welcome(_, path, apocalypse, round) => {
      val botConfig = configure(path, apocalypse, round)
      val strategies = createStrategyGroups(botConfig)
      Some(Response(environment = Some((botConfig, strategies))))
    }
    case _ => None
  }
}
