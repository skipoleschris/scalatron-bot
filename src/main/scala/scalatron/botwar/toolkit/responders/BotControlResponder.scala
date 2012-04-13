package scalatron.botwar.toolkit.responders

import scalatron.botwar.toolkit.protocol._
import scalatron.botwar.toolkit.strategies.StrategyChain
import scalatron.botwar.toolkit.domain.{Outcome, Request, DeltaOffset}


case class BotControlResponder(environment: BotEnvironment,
                               lastActions: IndexedSeq[Action] = Vector.empty) extends BotResponder with StrategyChain {
  def response = (lastActions map (_.toString)) mkString "|"

  protected def forCommand(command: Command): Option[BotResponder] = command match {
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
                         offset: Option[DeltaOffset] = None): BotResponder = {
    def lift[T](f: => T): Option[T] = Some(f)

    val req = Request(name, time, energy, view, offset, environment.trackedState)
    val allOutcomes = (for (
      strategyGroup <- environment.strategies;
      strategy <- forRequest(strategyGroup, req);
      outcomes <- lift(strategy.apply(req))
    ) yield outcomes).flatten

    Outcome.asResult(req.context.name, environment.sequenceGenerator, allOutcomes) map { r =>
      val nextEnv = (r.newMiniBots.foldLeft(environment) { (env, bot) =>
        env.updateTrackedState(bot._1, bot._2)
      }) updateTrackedState (r.name, r.trackedState) replace r.sequenceGenerator

      BotControlResponder(nextEnv, r.actions.toIndexedSeq)
    } getOrElse this.copy(lastActions = Vector.empty)
  }
}
