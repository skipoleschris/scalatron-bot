package scalatron.botwar.botPlugin

import configuration.{Configuration, BotConfig}
import strategies.StrategyChain
import protocol.CommandParser

class ControlFunctionFactory {
  def create: (String => String) = new Bot().respond _
}

class Bot extends CommandParser with Configuration with StrategyChain {
  def respond(input: String): String = {
    val actions = for (
      command <- parse(input);
      handler <- forCommand(command)
    ) yield handler.apply(command)

    actions map (_.mkString("|")) getOrElse  ""
  }
}
