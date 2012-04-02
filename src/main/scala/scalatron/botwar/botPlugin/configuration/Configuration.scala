package scalatron.botwar.botPlugin.configuration

import java.io.File
import com.typesafe.config.ConfigFactory

trait Configuration {
  private var _botConfig: BotConfig = _

  def botConfig = _botConfig

  def configure(path: String, apocalypse: Int, round: Int): Unit = {
    val config = ConfigFactory.parseFile(new File(path, "bot.conf"))
    _botConfig = BotConfig(apocalypse, round, config)

    installStrategies(_botConfig.strategyNames map (_.unwrapped().toString))
  }

  def installStrategies(strategies: List[String])
}
