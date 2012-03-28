package scalatron.botwar.botPlugin.configuration

import java.io.File
import com.typesafe.config.{ConfigValue, ConfigFactory}
import scalatron.botwar.botPlugin.strategies.Strategy
import scalatron.botwar.botPlugin.protocol.{Action, Command}

trait Configuration {

  def configure(path: String, apocalypse: Int, round: Int): Unit = {
    val config = ConfigFactory.parseFile(new File(path, "bot.conf"))
    val botConfig = BotConfig(apocalypse, round, config)

    installStrategies(botConfig.strategyNames map (_.unwrapped().toString), botConfig)
  }

  def installStrategies(strategies: List[String], config: BotConfig)
}
