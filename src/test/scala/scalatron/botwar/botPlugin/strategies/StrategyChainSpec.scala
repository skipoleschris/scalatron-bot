package scalatron.botwar.botPlugin.strategies

import org.specs2.Specification
import scalatron.botwar.botPlugin.protocol._
import scalatron.botwar.botPlugin.configuration.BotConfig

class StrategyChainSpec extends Specification { def is =

  "Specification for the Strategy Chain"                             ^
                                                                     endp ^
  "A Strategy Chain should"                                          ^
    "allow new strategies to be installed"                           ! installStrategies^
    "install a no-op strategy for one that cannot be found"          ! installNoOpStrategy^
    "find a strategy for a given command"                            ! findStrategy^
    "return None if no strategy can be found for a command"          ! noSuitableStrategy^
                                                                     end

  class TestStrategyChain extends StrategyChain

  def installStrategies = {
    val chain = new TestStrategyChain()
    chain.installStrategies("TestStrategy1" :: "TestStrategy3" :: Nil, BotConfig(5000, 1, null))

    chain.installedStrategies must haveSize(3)
  }

  def installNoOpStrategy = {
    val chain = new TestStrategyChain()
    chain.installStrategies("FooBar" :: Nil, BotConfig(5000, 1, null))
    chain.installedStrategies must haveSize(2)
  }

  def findStrategy = {
    val chain = new TestStrategyChain()
    chain.installStrategies("TestStrategy1" :: "TestStrategy3" :: Nil, BotConfig(5000, 1, null))

    chain.forCommand(Welcome("test", "test", 5000, 1)) must beSome[PartialFunction[Command, IndexedSeq[Action]]]
  }

  def noSuitableStrategy = {
    val chain = new TestStrategyChain()
    chain.installStrategies("TestStrategy1" :: "TestStrategy3" :: Nil, BotConfig(5000, 1, null))

    chain.forCommand(ReactBot("Foo", 1, 100, View("____M____"))) must_== None
  }
}

