package scalatron.botwar.botPlugin.configuration

import java.io.File
import com.typesafe.config.ConfigFactory

trait Configuration {
  def configure(path: String, apocalypse: Int, round: Int): BotConfig = {
    val config = ConfigFactory.parseFile(new File(path, "bot.conf"))
    BotConfig(apocalypse, round, config)
  }
}
