package scalatron.botwar.botPlugin.strategies

import org.specs2.Specification
import scalatron.botwar.botPlugin.protocol._

class StrategyChainSpec extends Specification { def is =

  "Specification for the Strategy Chain"                             ^
                                                                     endp ^
  "A Strategy Chain should"                                          ^
    "allow new strategies to be installed"                           ! installStrategies^
    "find a strategy for a given command"                            ! findStrategy^
    "return None if no strategy can be found for a command"          ! noSuitableStrategy^
                                                                     end

  class TestStrategyChain extends StrategyChain

  def installStrategies = {
    val chain = new TestStrategyChain()
    chain.installStrategies(strategy1 :: strategy3 :: Nil)

    chain.installedStrategies must_== (strategy1 :: strategy3 :: Nil)
  }

  def findStrategy = {
    val chain = new TestStrategyChain()
    chain.installStrategies(strategy1 :: strategy3 :: Nil)

    chain.forCommand(Welcome("test", "test", 5000, 1)) must_== Some(strategy1)
  }

  def noSuitableStrategy = {
    val chain = new TestStrategyChain()
    chain.installStrategies(strategy1 :: strategy3 :: Nil)

    chain.forCommand(ReactBot("Foo", 1, 100, View("____M____"))) must_== None
  }

  val strategy1: PartialFunction[Command, IndexedSeq[Action]] = {
    case Welcome(_, _, _, _) => Vector[Action]()
  }

  val strategy2: PartialFunction[Command, IndexedSeq[Action]] = {
    case ReactBot("Master", _, _, _) => Vector[Action](Move(1, 0), Status("foo"))
  }

  val strategy3: PartialFunction[Command, IndexedSeq[Action]] = {
    case Goodbye(_) => Vector[Action]()
  }
}

