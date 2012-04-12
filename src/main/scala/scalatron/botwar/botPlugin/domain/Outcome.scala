package scalatron.botwar.botPlugin.domain

import scalatron.botwar.botPlugin.protocol._

trait Outcome {
  def encode(result: OutcomeResult): OutcomeResult
  def compatibleWithMaster = true
  def compatibleWithMiniBot = true
}

case class MoveOutcome(offset: DeltaOffset) extends Outcome {
  def encode(result: OutcomeResult) = result withAction Move(offset.x, offset.y)
}

case class ExplodeOutcome(size: Int) extends Outcome {
  def encode(result: OutcomeResult) = result withAction Explode(size)
  override val compatibleWithMaster = false
}

case class SpawnOutcome(position: DeltaOffset, energy: Int, state: State) extends Outcome {
  def encode(result: OutcomeResult) = {
    val identity = MiniBotNameEncodeDecode.newName(result.sequenceGenerator, state.running)
    result withAction Spawn(position.x, position.y, identity._2, energy) withNewMiniBot (identity, state.tracked)
  }
  override val compatibleWithMiniBot = false
}

case class StatusOutcome(text: String) extends Outcome {
  def encode(result: OutcomeResult) = result withAction Status(text)
}

case class SayOutcome(text: String) extends Outcome {
  def encode(result: OutcomeResult) = result withAction Say(text)
}

case class UpdateRunningState(name: String, state: Map[String, String]) extends Outcome {
  def encode(result: OutcomeResult) = result withAction SetName(MiniBotNameEncodeDecode.encode(name, state))
  override val compatibleWithMaster = false
}

case class UpdateTrackedState(state: Map[String, String]) extends Outcome {
  def encode(result: OutcomeResult) = result withTrackedState state
}

case class OutcomeResult(name: String,
                         sequenceGenerator: Stream[Int],
                         actions: Set[Action] = Set.empty,
                         newMiniBots: Set[(String, Map[String, String])] = Set.empty,
                         trackedState: Map[String, String] = Map.empty) {
  def withAction(action: Action) = copy(actions = actions + action)
  def withNewMiniBot(identity: (String, String, Stream[Int]), trackedState: Map[String, String]) =
    copy(sequenceGenerator = identity._3, newMiniBots = newMiniBots + (identity._1 -> trackedState))
  def withTrackedState(updatedState: Map[String, String]) = copy(trackedState = updatedState)
}

object Outcome {
  def asResult(name: String, sequenceGenerator: Stream[Int], outcomes: IndexedSeq[Outcome]): Option[OutcomeResult] = {
    def encodeOutcome(result: OutcomeResult, outcome: Outcome) = outcome.encode(result)
    def compatibility(forMaster: Boolean)(outcome: Outcome) =
      if ( forMaster ) outcome.compatibleWithMaster else outcome.compatibleWithMiniBot

    if ( outcomes.isEmpty ) None
    else Some((outcomes filter compatibility(name == "Master")).foldLeft(OutcomeResult(name, sequenceGenerator))(encodeOutcome))
  }
}
