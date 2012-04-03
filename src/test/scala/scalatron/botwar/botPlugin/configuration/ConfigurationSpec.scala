package scalatron.botwar.botPlugin.configuration

import org.specs2.Specification

class ConfigurationSpec extends Specification with Configuration { def is =

  "Specification for Configuration"                                  ^
                                                                     endp^
  "A Configuration should"                                           ^
    "load the bot configuration file"                                ! loadConfig^
                                                                     end

  def loadConfig = {
    val botConfig = configure("src/test/resources", 5000, 1)

    (botConfig.apocalypse must_== 5000) and
    (botConfig.round must_== 1) and
    (botConfig.strategyNames must_== ("RandomMovementStrategy" :: Nil)) and
    (botConfig.config must not beNull)
  }
}

