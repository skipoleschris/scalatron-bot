package scalatron.botwar.botPlugin

import responders.{InitialisingResponder, BotResponder}

class ControlFunctionFactory {
  def create: (String => String) = new Bot().respond _
}

class Bot {
  private var responder: BotResponder = InitialisingResponder

  def respond(input: String): String = {
    responder = responder.respond(input)
    responder.response
  }
}








