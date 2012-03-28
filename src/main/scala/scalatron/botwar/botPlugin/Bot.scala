package scalatron.botwar.botPlugin

import handlers.HandlerChain
import protocol.CommandParser

class ControlFunctionFactory {
  def create: (String => String) = new Bot().respond _
}

class Bot extends CommandParser with HandlerChain {

  def respond(input: String): String = {
    val actions = for (
      command <- parse(input);
      handler <- forCommand(command)
    ) yield handler.apply(command)

    actions map (_.mkString("|")) getOrElse  ""
  }
}
