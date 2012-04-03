package scalatron.botwar.botPlugin.domain

import org.specs2.Specification
import scalatron.botwar.botPlugin.protocol._
import collection.immutable.{TreeSet, TreeMap}

class OutcomeSpec extends Specification { def is =

  "Specification for Outcomes"                                       ^
                                                                     endp^
  "An Outcome should"                                                ^
    "encode the move into the result"                                ! encodeMove^
    "encode the explode into the result"                             ! encodeExplode^
    "encode the spawn into the result"                               ! encodeSpawn^
    "encode the status into the result"                              ! encodeStatus^
    "encode the say into the result"                                 ! encodeSay^
    "encode the running state into the result"                       ! encodeRunningState^
    "update tracked state in the result"                             ! updateTrackedState^
                                                                     endp^
  "A Set of Outcomes should"                                         ^
    "be convertable into a result"                                   ! asResult^
                                                                     end

  def encodeMove = {
    val result = OutcomeResult("Master", Stream.from(1))
    val updated = MoveOutcome(DeltaOffset(1, -1)).encode(result)

    updated.actions must_== Set(Move(1, -1))
  }

  def encodeExplode = {
    val result = OutcomeResult("Master", Stream.from(1))
    val updated = ExplodeOutcome(9).encode(result)

    updated.actions must_== Set(Explode(9))
  }

  def encodeSpawn = {
    val result = OutcomeResult("Master", Stream.from(1))
    val runningState = Map("foo" -> "FOO")
    val trackedState = Map("bar" -> "BAR")
    val updated = SpawnOutcome(DeltaOffset(1, -1), 100, State(runningState, trackedState)).encode(result)

    (updated.actions must_== Set(Spawn(1, -1, "1:foo/FOO", 100))) and
    (updated.sequenceGenerator.head must_== 2) and
    (updated.newMiniBots must_== Set("1" -> trackedState))
  }

  def encodeStatus = {
    val result = OutcomeResult("Master", Stream.from(1))
    val updated = StatusOutcome("Test").encode(result)

    updated.actions must_== Set(Status("Test"))
  }

  def encodeSay = {
    val result = OutcomeResult("Master", Stream.from(1))
    val updated = SayOutcome("Test").encode(result)

    updated.actions must_== Set(Say("Test"))
  }

  def encodeRunningState = {
    val result = OutcomeResult("Master", Stream.from(1))
    val runningState = TreeMap("foo" -> "FOO", "bar" -> "BAR")
    val updated = UpdateRunningState("1", runningState).encode(result)

    updated.actions must_== Set(SetName("1:bar/BAR;foo/FOO"))
  }

  def updateTrackedState = {
    val result = OutcomeResult("Master", Stream.from(1))
    val trackedState = TreeMap("foo" -> "FOO", "bar" -> "BAR")
    val updated = UpdateTrackedState(trackedState).encode(result)

    updated.trackedState must_== trackedState
  }

  def asResult = {
    val result = Outcome.asResult("Master", Stream.from(1), Set(MoveOutcome(DeltaOffset(1, -1)), SayOutcome("Test")))

    (result.name must_== "Master") and
    (result.sequenceGenerator.head must_== 1) and
    (result.actions must containAllOf(Seq(Move(1, -1), Say("Test"))))

  }
}
