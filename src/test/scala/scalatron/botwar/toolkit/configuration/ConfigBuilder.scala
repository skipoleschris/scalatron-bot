package scalatron.botwar.toolkit.configuration

import scala.collection.JavaConverters._
import com.typesafe.config.ConfigFactory

object ConfigBuilder {
  def apply(strategies: IndexedSeq[(String, List[String])]) = {
    val order = (strategies map (_._1)).asJava
    val groups = strategies.foldLeft(Map.empty[String, java.util.List[String]])((map, s) => map + (s._1 -> s._2.asJava)).asJava
    val configEntries = Map("bot.strategies" -> Map("order" -> order, "groups" -> groups).asJava).asJava
    ConfigFactory.parseMap(configEntries)
  }
}
