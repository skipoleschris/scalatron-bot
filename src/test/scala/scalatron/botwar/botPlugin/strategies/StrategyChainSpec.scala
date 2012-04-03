package scalatron.botwar.botPlugin.strategies

import scala.collection.JavaConverters._
import org.specs2.Specification
import scalatron.botwar.botPlugin.configuration.BotConfig
import com.typesafe.config.ConfigFactory
import scalatron.botwar.botPlugin.domain.Request

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
    val configEntries = Map(("bot.strategies" -> ("TestStrategy1" :: "TestStrategy2" :: Nil).asJava)).asJava
    val config = ConfigFactory.parseMap(configEntries)
    val strategies = createStrategies(new BotConfig(5000, 1, config))

    strategies must haveSize(2)
  }

  def installNoOpStrategy = {
    val configEntries = Map(("bot.strategies" -> ("FooBar" :: Nil).asJava)).asJava
    val config = ConfigFactory.parseMap(configEntries)
    val strategies = createStrategies(new BotConfig(5000, 1, config))

    strategies must haveSize(1)
  }

  def findStrategy = {
    val configEntries = Map(("bot.strategies" -> ("TestStrategy1" :: "TestStrategy2" :: Nil).asJava)).asJava
    val config = ConfigFactory.parseMap(configEntries)
    val strategies = createStrategies(new BotConfig(5000, 1, config))
    val request = Request("Master", 1, 100, "____M____", None)

    forRequest(strategies, request) must beSome[Strategy#StrategyFunction]
  }

  def noSuitableStrategy = {
    val configEntries = Map(("bot.strategies" -> ("TestStrategy1" :: "TestStrategy2" :: Nil).asJava)).asJava
    val config = ConfigFactory.parseMap(configEntries)
    val strategies = createStrategies(new BotConfig(5000, 1, config))
    val request = Request("1:", 1, 100, "____M____", None)

    forRequest(strategies, request) must_== None
  }
}

