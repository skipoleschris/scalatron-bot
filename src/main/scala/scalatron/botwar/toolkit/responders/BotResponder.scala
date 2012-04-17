package scalatron.botwar.toolkit.responders

import scalatron.botwar.toolkit.protocol.{CommandParser, Command}

trait BotResponder extends CommandParser {
  def respond(input: String): Response = {
    val responder = for (
      command <- parse(input);
      handler <- forCommand(command)
    ) yield handler

    responder getOrElse Response.empty
  }

  protected def forCommand(command: Command): Option[Response]
}

object BotResponder {
  def apply(context: BotContext, sequenceGenerator: Stream[Int]): BotResponder =
    if ( context.initialised ) BotControlResponder(context, sequenceGenerator) else InitialisingResponder(context)
}

