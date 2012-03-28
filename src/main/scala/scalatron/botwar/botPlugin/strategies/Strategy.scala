package scalatron.botwar.botPlugin.strategies

import scalatron.botwar.botPlugin.protocol.{Command, Action}

trait Strategy {
  def react: PartialFunction[Command, IndexedSeq[Action]]
}
