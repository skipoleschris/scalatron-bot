package scalatron.botwar.botPlugin.configuration

import com.typesafe.config.{ConfigValue, Config}


case class BotConfig(apocalypse: Int, round: Int, config: Config) {
  def strategyNames = List(config.getList("bot.strategies").toArray(Array[ConfigValue]()) : _*)
}
