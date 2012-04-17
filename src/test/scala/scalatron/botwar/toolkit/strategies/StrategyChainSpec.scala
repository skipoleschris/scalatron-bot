package scalatron.botwar.toolkit.strategies

import org.specs2.Specification
import scalatron.botwar.toolkit.domain.Request
import scalatron.botwar.toolkit.configuration.{ConfigBuilder, BotConfig}

class StrategyChainSpec extends Specification with StrategyChain { def is =

  "Specification for the Strategy Chain"                             ^
                                                                     endp ^
  "A Strategy Chain should"                                          ^
    "create new strategies from a given configuration"               ! strategyCreation^
    "install a no-op strategy for one that cannot be found"          ! installNoOpStrategy^
    "find a strategy for a given request"                            ! findStrategy^
    "return None if no strategy can be found for a command"          ! noSuitableStrategy^
                                                                     end

  def strategyCreation = {
    val config = ConfigBuilder(Vector("test" -> ("TestStrategy1" :: "TestStrategy2" :: Nil)))
    val strategies = createStrategyGroups(new BotConfig(5000, 1, config))

    (strategies must haveSize(1)) and
    (strategies.head must haveSize(2))
  }

  def installNoOpStrategy = {
    val config = ConfigBuilder(Vector("test" -> ("FooBar" :: Nil)))
    val strategies = createStrategyGroups(new BotConfig(5000, 1, config))

    (strategies must haveSize(1)) and
    (strategies.head must haveSize(1))
  }

  def findStrategy = {
    val config = ConfigBuilder(Vector("test" -> ("TestStrategy1" :: "TestStrategy2" :: Nil)))
    val strategies = createStrategyGroups(new BotConfig(5000, 1, config))
    val request = Request("Master", 1, 100, "____M____", None, _ => Map.empty)

    forRequest(strategies.head, request) must beSome[Strategy#StrategyFunction]
  }

  def noSuitableStrategy = {
    val config = ConfigBuilder(Vector("test" -> ("TestStrategy1" :: "TestStrategy2" :: Nil)))
    val strategies = createStrategyGroups(new BotConfig(5000, 1, config))
    val request = Request("1:", 1, 100, "____M____", None, _ => Map.empty)

    forRequest(strategies.head, request) must_== None
  }
}

