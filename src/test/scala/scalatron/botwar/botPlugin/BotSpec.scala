package scalatron.botwar.botPlugin

import configuration.BotConfig
import org.specs2.Specification

class BotSpec extends Specification { def is =

  "Specification for Bot"                                            ^
                                                                     endp^
  "A Bot should"                                                     ^
    "process a sequence of commands"                                 ! respond^
    "return no action if no strategies match the action"             ! noMatch^
                                                                     end

  def respond = {
    val bot = new Bot()
    val welcomeResponse = bot.respond("Welcome(name=Test,path=src/test/resources/bottest,apocalypse=3,round=1)")
    val round2Response = bot.respond("React(entity=Master,time=2,view=____M____,energy=1000)")
    val round3MasterResponse = bot.respond("React(entity=Master,time=3,view=____M____,energy=1000)")
    val round3MiniBotResponse = bot.respond("React(entity=1:,time=3,view=____M____,energy=1000,dx=1,dy=-1)")

    (welcomeResponse must_== "") and
    (round2Response must_== "Move(dx=1,dy=-1)|Say(text=Hello)|Spawn(dx=1,dy=-1,name=1:,energy=200)") and
    (round3MasterResponse must_== "Move(dx=1,dy=-1)|Say(text=Hello)|Spawn(dx=1,dy=-1,name=2:,energy=200)") and
    (round3MiniBotResponse must_== "Move(dx=1,dy=-1)|Say(text=Hello)|Explode(size=3)")
  }

  def noMatch = {
//    val bot = new Bot()
//    bot.installStrategies("TestStrategy1" :: "TestStrategy2" :: "TestStrategy3" :: Nil)
//    bot.respond("React(entity=Foo,time=1,energy=500,view=____M____,dx=10,dy=0)") must_== ""
    pending
  }
}

