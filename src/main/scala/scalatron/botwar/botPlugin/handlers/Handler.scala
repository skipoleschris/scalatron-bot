package scalatron.botwar.botPlugin.handlers

import scalatron.botwar.botPlugin.protocol.{Command, Action}

trait Handler {
  def react: PartialFunction[Command, IndexedSeq[Action]]
}
