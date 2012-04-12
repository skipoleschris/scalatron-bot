package scalatron.botwar.botPlugin.configuration

import scala.collection.JavaConverters._
import com.typesafe.config.{ConfigList, ConfigValue, Config}
import java.util.Map.Entry

case class BotConfig(apocalypse: Int, round: Int, config: Config) {
  def strategyGroups: Map[String, List[String]] = {
    def strategies = config.getConfig("bot.strategies").entrySet().asScala
    def values(entry: Entry[String, ConfigValue]) = entry.getValue.asInstanceOf[ConfigList].toArray(Array[ConfigValue]())
    def stringValues(entry: Entry[String, ConfigValue]) = (values(entry) map (_.unwrapped().toString)).toList

    (strategies map (entry => (entry.getKey, stringValues(entry)))).toMap
  }
}
