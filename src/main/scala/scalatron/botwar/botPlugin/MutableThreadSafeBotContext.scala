package scalatron.botwar.botPlugin

import scalatron.botwar.toolkit.configuration.BotConfig
import scalatron.botwar.toolkit.strategies.StrategyChain
import scalatron.botwar.toolkit.responders.{Response, BotContext}

trait MutableThreadSafeBotContext extends BotContext {
  private var _config: Option[BotConfig] = Option.empty
  private var _strategies: Option[StrategyChain#StrategySeq] = Option.empty
  private var _sequenceGenerator = Stream.from(0)
  private[botPlugin] val _trackedState = new java.util.concurrent.ConcurrentHashMap[String, Map[String, String]]()

  def initialised = _config.isDefined
  def config = _config getOrElse (throw new IllegalStateException("Not initialised"))
  def strategies = _strategies getOrElse (throw new IllegalStateException("Not initialised"))
  def trackedState(botName: String) = Option(_trackedState.get(botName)) getOrElse Map.empty

  private[botPlugin] def nextIdBlock = synchronized {
    val ids = _sequenceGenerator.take(10)
    _sequenceGenerator = _sequenceGenerator.drop(10)
    ids
  }

  private[botPlugin] def updateFrom(response: Response): Unit = {
    response.forEnvironment(initialiseEnvironment)
    response.foreachTrackedState(updateTrackedState)
  }

  private def updateTrackedState(botName: String, state: Map[String, String]): Unit =
    if ( state.isEmpty ) _trackedState.remove(botName) else _trackedState.put(botName, state)

  private def initialiseEnvironment(config: BotConfig, strategies: StrategyChain#StrategySeq): Unit = {
    _config = Some(config)
    _strategies = Some(strategies)
  }
}

object MutableThreadSafeBotContext extends MutableThreadSafeBotContext {
  private[botPlugin] def reset(): Unit = {
    _config = Option.empty
    _strategies = Option.empty
    _sequenceGenerator = Stream.from(0)
    _trackedState.clear()
  }
}
