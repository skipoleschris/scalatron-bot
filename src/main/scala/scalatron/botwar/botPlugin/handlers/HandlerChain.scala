package scalatron.botwar.botPlugin.handlers

import scalatron.botwar.botPlugin.protocol._

trait HandlerChain {

  type Handler = PartialFunction[Command, IndexedSeq[Action]]
//  private var handlers = List[Handler]()
  private var handlers = List[Handler](new RandomMovementHandler().react)

  def installHandlers(newHandlers: List[Handler]): Unit =
    handlers = newHandlers ++ handlers

  def forCommand(command: Command): Option[Handler] = handlers find (_.isDefinedAt(command))

  private[handlers] def installedHandlers = handlers
}
