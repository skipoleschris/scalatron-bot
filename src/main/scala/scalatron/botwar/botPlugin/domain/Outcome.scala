package scalatron.botwar.botPlugin.domain

import scalatron.botwar.botPlugin.protocol._

trait Outcome {
  def encode(result: OutcomeResult): OutcomeResult
}

case class MoveOutcome(offset: DeltaOffset) extends Outcome {
  def encode(result: OutcomeResult) = result withAction Move(offset.x, offset.y)
}

case class ExplodeOutcome(size: Int) extends Outcome {
  def encode(result: OutcomeResult) = result withAction Explode(size)
}

case class SpawnOutcome(position: DeltaOffset, energy: Int, state: State) extends Outcome {
  def encode(result: OutcomeResult) = {
    val name = MiniBotNameEncodeDecode.newName(state.running)
    result withAction Spawn(position.x, position.y, name, energy) withNewMiniBot (name, state.tracked)
  }
}

case class StatusOutcome(text: String) extends Outcome {
  def encode(result: OutcomeResult) = result withAction Status(text)
}

case class SayOutcome(text: String) extends Outcome {
  def encode(result: OutcomeResult) = result withAction Say(text)
}

case class UpdateRunningState(name: String, state: Map[String, String]) extends Outcome {
  def encode(result: OutcomeResult) = result withAction SetName(MiniBotNameEncodeDecode.encode(name, state))
}

case class UpdateTrackedState(state: Map[String, String]) extends Outcome {
  def encode(result: OutcomeResult) = result withTrackedState state
}

case class OutcomeResult(name: String,
                         actions: Set[Action] = Set(),
                         newMiniBots: Set[(String, Map[String, String])] = Set(),
                         trackedState: Map[String, String] = Map()) {
  def withAction(action: Action) = copy(actions = actions + action)
  def withNewMiniBot(name: String, trackedState: Map[String, String]) =
    copy(newMiniBots = newMiniBots + (name -> trackedState))
  def withTrackedState(updatedState: Map[String, String]) = copy(trackedState = updatedState)
}

object Outcome {
  def asResult(name: String, outcomes: Set[Outcome]): OutcomeResult = {
    def encodeOutcome(result: OutcomeResult, outcome: Outcome) = outcome.encode(result)
    outcomes.foldLeft(OutcomeResult(name))(encodeOutcome)
  }
}
