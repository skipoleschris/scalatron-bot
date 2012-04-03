package scalatron.botwar.botPlugin.responders

import scalatron.botwar.botPlugin.protocol._
import scalatron.botwar.botPlugin.domain.{Outcome, Request, DeltaOffset}
import scalatron.botwar.botPlugin.strategies.StrategyChain


case class BotControlResponder(environment: BotEnvironment,
                               lastActions: IndexedSeq[Action] = Vector()) extends BotResponder with StrategyChain {
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

    val result = for (
      req <- lift(Request(name, time, energy, view, offset));
      strategy <- forRequest(environment.strategies, req);
      outcomes <- lift(strategy.apply(req));
      result <- lift(Outcome.asResult(req.context.name, environment.sequenceGenerator, outcomes))
    ) yield result

    result map { r =>
      val nextEnv = (r.newMiniBots.foldLeft(environment) { (env, bot) =>
        env.updateTrackedState(bot._1, bot._2)
      }) replace r.sequenceGenerator

      BotControlResponder(nextEnv, r.actions.toIndexedSeq)
    } getOrElse this
  }
}
