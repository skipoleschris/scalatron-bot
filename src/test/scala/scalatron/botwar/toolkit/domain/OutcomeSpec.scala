package scalatron.botwar.toolkit.domain

import org.specs2.Specification
import scalatron.botwar.toolkit.protocol._
import collection.immutable.TreeMap

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
    "exclude outcomes not compatible with a master"                  ! masterCompatibility^
    "exclude outcomes not compatible with a mini-bot"                ! miniBotCompatibility^
    "not convert to a result if empty"                               ! emptyOutcomes^
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
    val updated = MaintainTrackedState(trackedState).encode(result)

    updated.trackedState must_== trackedState
  }

  def asResult = {
    val result = Outcome.asResult("Master", Stream.from(1), Vector(MoveOutcome(DeltaOffset(1, -1)), SayOutcome("Test"))).get

    (result.name must_== "Master") and
    (result.sequenceGenerator.head must_== 1) and
    (result.actions must containAllOf(Seq(Move(1, -1), Say("Test"))))
  }

  def masterCompatibility = {
    val allOutcomes = Vector[Outcome](MoveOutcome(DeltaOffset(1, -1)),
                                      ExplodeOutcome(5),
                                      SpawnOutcome(DeltaOffset(1, -1), 100, State(Map("foo" -> "FOO"), Map("bar" -> "BAR"))),
                                      StatusOutcome("status"),
                                      SayOutcome("say"),
                                      UpdateRunningState("1", Map.empty),
                                      MaintainTrackedState(Map("baz" -> "BAZ")))
    val result = Outcome.asResult("Master", Stream.from(1), allOutcomes).get
    (result.actions must haveSize(4)) and
    (result.actions must containAllOf(Seq(Move(1, -1),
                                          Spawn(1, -1, "1:foo/FOO", 100),
                                          Status("status"),
                                          Say("say")))) and
    (result.trackedState must contain(("baz" -> "BAZ"))) and
    (result.newMiniBots must haveSize(1))
  }

  def miniBotCompatibility = {
    val allOutcomes = Vector[Outcome](MoveOutcome(DeltaOffset(1, -1)),
                                      ExplodeOutcome(5),
                                      SpawnOutcome(DeltaOffset(1, -1), 100, State(Map("foo" -> "FOO"), Map("bar" -> "BAR"))),
                                      StatusOutcome("status"),
                                      SayOutcome("say"),
                                      UpdateRunningState("1", Map("bam" -> "BAM")),
                                      MaintainTrackedState(Map("baz" -> "BAZ")))
    val result = Outcome.asResult("1", Stream.from(1), allOutcomes).get
    (result.actions must haveSize(5)) and
    (result.actions must containAllOf(Seq(Move(1, -1),
                                          Explode(5),
                                          Status("status"),
                                          Say("say"),
                                          SetName("1:bam/BAM")))) and
      (result.trackedState must contain(("baz" -> "BAZ"))) and
      (result.newMiniBots must beEmpty)
  }

  def emptyOutcomes = {
    Outcome.asResult("1", Stream.from(1), Vector.empty) must_== None
  }
}

