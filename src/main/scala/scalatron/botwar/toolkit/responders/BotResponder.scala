package scalatron.botwar.toolkit.responders

import scalatron.botwar.toolkit.protocol.{CommandParser, Command}

trait BotResponder extends CommandParser {
  def response: String
  def respond(input: String): BotResponder = {
    val responder = for (
      command <- parse(input);
      handler <- forCommand(command)
    ) yield handler

    responder getOrElse this
  }

  protected def forCommand(command: Command): Option[BotResponder]
}

