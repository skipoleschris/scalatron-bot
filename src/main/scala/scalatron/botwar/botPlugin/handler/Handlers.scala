package scalatron.botwar.botPlugin.handler

import scalatron.botwar.botPlugin.protocol._
import scalatron.botwar.botPlugin.domain._
import scalatron.botwar.botPlugin.configuration.Configuration
import scalatron.botwar.botPlugin.strategies.StrategyChain

trait Handlers {
  self: Configuration with StrategyChain =>

  type Handler = Command => IndexedSeq[Action]

  private val trackedState = scala.collection.mutable.Map[String, Map[String, String]]()

  def forCommand(command: Command): Option[Handler] = command match {
    case _: Welcome => Some(lifecycleHandler _)
    case _: Goodbye => Some(lifecycleHandler _)
    case _: ReactBot => Some(botHandler _)
    case _: ReactMiniBot => Some(botHandler _)
    case _ => None
  }

  private def lifecycleHandler(command: Command) = command match {
    case Welcome(_, path, apocalypse, round) => configure(path, apocalypse, round); Vector[Action]()
    //TODO: record the config used and the final result
    case Goodbye(energy) => Vector[Action]()
    case _ => Vector[Action]()
  }

  private def botHandler(command: Command) = command match {
    case ReactBot(name, time, energy, view) => processReact(name, time, energy, view)
    case ReactMiniBot(name, time, energy, dx, dy, view) => processReact(name, time, energy, view, Some(DeltaOffset(dx, dy)))
    case _ => Vector[Action]()
  }

 private def processReact(name: String,
                          time: Int,
                          energy: Int,
                          view: String,
                          offset: Option[DeltaOffset] = None): IndexedSeq[Action] = {
   def lift[T](f: => T): Option[T] = Some(f)

   val result = for (
     req <- lift(Request(name, time, energy, view, offset));
     strategy <- forRequest(req);
     outcomes <- lift(strategy.apply(req));
     result <- lift(Outcome.asResult(req.context.name, outcomes))
   ) yield result

   result map { r =>
     r.newMiniBots foreach (bot => trackedState.update(bot._1, bot._2))
     trackedState.update(r.name, r.trackedState)
     r.actions.toIndexedSeq
   } getOrElse Vector[Action]()
 }
}








