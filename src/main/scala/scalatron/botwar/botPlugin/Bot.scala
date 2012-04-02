package scalatron.botwar.botPlugin

import configuration.Configuration
import handler.Handlers
import strategies.StrategyChain
import protocol.CommandParser

class ControlFunctionFactory {
  def create: (String => String) = new Bot().respond _
}

class Bot extends CommandParser with Configuration with Handlers with StrategyChain {
  def respond(input: String): String = {
    val actions = for (
      command <- parse(input);
      handler <- forCommand(command)
    ) yield handler(command)

    actions map (_.mkString("|")) getOrElse  ""
  }
}
