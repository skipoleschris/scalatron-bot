package scalatron.botwar.botPlugin.configuration

import org.specs2.Specification

class ConfigurationSpec extends Specification { def is =

  "Specification for Configuration"                                  ^
                                                                     endp^
  "A Configuration should"                                           ^
    "load the bot configuration file"                                ! loadConfig^
    "install the strategies specified in the config file"            ! installStrategies^
                                                                     end

  class TestConfiguration extends Configuration {
    var installedStrategies: List[String] = Nil
    def installStrategies(strategies: List[String], config: BotConfig): Unit = installedStrategies = strategies
  }

  def loadConfig = {
    val config = new TestConfiguration()
    config.configure("src/test/resources", 5000, 1)

    val botConfig = config.botConfig
    (botConfig.apocalypse must_== 5000) and
    (botConfig.round must_== 1)
  }

  def installStrategies = {
    pending
  }
}

