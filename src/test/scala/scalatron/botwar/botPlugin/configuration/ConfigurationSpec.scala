package scalatron.botwar.botPlugin.configuration

import org.specs2.Specification

class ConfigurationSpec extends Specification with Configuration { def is =

  "Specification for Configuration"                                  ^
                                                                     endp^
  "A Configuration should"                                           ^
    "load the bot configuration file"                                ! loadConfig^
    "define the different strategy groups"                           ! strategyGroups^
                                                                     end

  def loadConfig = {
    val botConfig = configure("src/test/resources/configtest", 5000, 1)

    (botConfig.apocalypse must_== 5000) and
    (botConfig.round must_== 1) and
    (botConfig.config must not beNull)
  }

  def strategyGroups = {
    val botConfig = configure("src/test/resources/configtest", 5000, 1)

    botConfig.strategyGroups must_== Vector("others" -> ("NoOpStrategy" :: Nil),
                                            "movement" -> ("RandomMovementStrategy" :: Nil))
  }
}

