package scalatron.botwar.toolkit.configuration

import scala.collection.JavaConverters._
import com.typesafe.config.Config

case class BotConfig(apocalypse: Int, round: Int, config: Config) {
  def strategyGroups: IndexedSeq[(String, List[String])] = {
    def asTuple(name: String, l: java.util.List[String]): (String, List[String]) = (name, l.toArray(Array[String]()).toList)

    val groupsConfig = config.getConfig("bot.strategies.groups")
    val groupNames = groupsConfig.entrySet.asScala map (_.getKey)
    val groups = (groupNames map (name => asTuple(name, groupsConfig.getStringList(name)))).toMap
    val ordering = config.getStringList("bot.strategies.order").toArray(Array[String]())

    (ordering map (name => name -> groups(name))).toIndexedSeq
  }
}
