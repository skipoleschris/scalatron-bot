package scalatron.botwar.botPlugin

import scalatron.botwar.toolkit.responders.BotResponder

class ControlFunctionFactory {
  def create: (String => String) = new Bot().respond _
}

class Bot {
  def respond(input: String): String = {
    val ids = MutableThreadSafeBotContext.nextIdBlock
    val response = BotResponder(MutableThreadSafeBotContext, ids) respond input
    MutableThreadSafeBotContext.updateFrom(response)
    response.response
  }
}
