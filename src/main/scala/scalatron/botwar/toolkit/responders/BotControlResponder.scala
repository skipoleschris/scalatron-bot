package scalatron.botwar.toolkit.responders

import scalatron.botwar.toolkit.protocol._
import scalatron.botwar.toolkit.strategies.StrategyChain
import scalatron.botwar.toolkit.domain.{Outcome, Request, DeltaOffset}


case class BotControlResponder(context: BotContext, sequenceGenerator: Stream[Int]) extends BotResponder with StrategyChain {
  protected def forCommand(command: Command): Option[Response] = command match {
    case ReactBot(name, time, energy, view) =>
      Some(controlBot(name, time, energy, view))
    case ReactMiniBot(name, time, energy, dx, dy, view) =>
      Some(controlBot(name, time, energy, view, Some(DeltaOffset(dx, dy))))
    case Goodbye(energy) => None // TODO: log strategies used and energy accumulated
    case _ => None
  }

  private def controlBot(name: String,
                         time: Int,
                         energy: Int,
                         view: String,
                         offset: Option[DeltaOffset] = None): Response = {
    def lift[T](f: => T): Option[T] = Some(f)

    val req = Request(name, time, energy, view, offset, context.trackedState)
    val allOutcomes = (for (
      strategyGroup <- context.strategies;
      strategy <- forRequest(strategyGroup, req);
      outcomes <- lift(strategy.apply(req))
    ) yield outcomes).flatten

    Outcome.asResult(req.context.name, sequenceGenerator, allOutcomes) map { r =>
      val response = (r.newMiniBots.foldLeft(Response.empty) { (resp, bot) =>
        resp.updateTrackedState(bot._1, bot._2)
      }) updateTrackedState (r.name, r.trackedState)

      response.copy(actions = r.actions.toIndexedSeq)
    } getOrElse Response.empty
  }
}
