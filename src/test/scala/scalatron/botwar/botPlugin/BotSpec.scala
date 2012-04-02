package scalatron.botwar.botPlugin

import configuration.BotConfig
import org.specs2.Specification

class BotSpec extends Specification { def is =

  "Specification for Bot"                                            ^
                                                                     endp^
  "A Bot should"                                                     ^
    "pass the command to all installed strategies"                   ! respond^
    "return no action if no strategies match the action"             ! noMatch^
                                                                     end

  def respond = {
    val bot = new Bot()
    bot.installStrategies("TestStrategy1" :: "TestStrategy2" :: "TestStrategy3" :: Nil)
    bot.respond("React(entity=Master,time=1,energy=500,view=____M____)") must_==
      "Move(dx=1,dy=0)|Status(text=foo)"
  }

  def noMatch = {
    val bot = new Bot()
    bot.installStrategies("TestStrategy1" :: "TestStrategy2" :: "TestStrategy3" :: Nil)
    bot.respond("React(entity=Foo,time=1,energy=500,view=____M____,dx=10,dy=0)") must_== ""
  }
}

