package scalatron.botwar.botPlugin

import org.specs2.Specification
import scalatron.botwar.toolkit.responders.Response
import scalatron.botwar.toolkit.configuration.BotConfig

class MutableThreadSafeBotContextSpec extends Specification { def is =

  "Specification for Mutable Thread Safe Bot Context"                ^
                                                                     endp^
  "A Mutable Thread Safe Bot Context should"                         ^
    "start in an uninitialised state"                                ! startState^
    "allocate ids in blocks of 10"                                   ! allocateIds^
    "initialise from environment details contained in a response"    ! initialise^
    "updated tracked state from details contained in a response"     ! updateTrackedState^
                                                                     end

  class TestMutableThreadSafeBotContext extends MutableThreadSafeBotContext {
    def setTrackedState(state: Map[String, Map[String, String]]): Unit =
      state foreach { entry => _trackedState.put(entry._1, entry._2) }
  }

  def startState = {
    val context = new TestMutableThreadSafeBotContext()
    context.initialised must beFalse
  }

  def allocateIds = {
    val context = new TestMutableThreadSafeBotContext()
    val block1 = context.nextIdBlock
    val block2 = context.nextIdBlock
    (block1.head must_== 0) and (block2.head must_== 10)
  }

  def initialise = {
    val context = new TestMutableThreadSafeBotContext()
    val config = new BotConfig(5000, 1, null)
    val strategies = IndexedSeq()
    context.updateFrom(Response(environment = Some(config, strategies)))

    (context.initialised must beTrue) and
    (context.config must be(config)) and
    (context.strategies must be(strategies))
  }

  def updateTrackedState = {
    val context = new TestMutableThreadSafeBotContext()
    context.setTrackedState(Map("0" -> Map("bar" -> "BAR")))
    context.updateFrom(Response(trackedState = Map("Master" -> Map("foo" -> "FOO"), "0" -> Map.empty)))

    (context.trackedState("Master") must_== Map("foo" -> "FOO")) and
    (context.trackedState("0") must beEmpty)
  }
}

