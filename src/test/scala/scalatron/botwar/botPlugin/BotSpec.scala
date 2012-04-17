package scalatron.botwar.botPlugin

import org.specs2.Specification

class BotSpec extends Specification { def is =

                                                                     sequential^
  "Specification for Bot"                                            ^
                                                                     endp^
  "A Bot should"                                                     ^
    "process a sequence of commands"                                 ! respond^
    "return no action if no strategies match the action"             ! noMatch^
    "maintain tracked state across multiple commands"                ! trackedState^
                                                                     end

  def respond = {
    MutableThreadSafeBotContext.reset()
    val bot = new Bot()
    val welcomeResponse = bot.respond("Welcome(name=Test,path=src/test/resources/bottest,apocalypse=3,round=1)")
    val round2Response = bot.respond("React(entity=Master,time=2,view=____M____,energy=1000)")
    val round3MasterResponse = bot.respond("React(entity=Master,time=3,view=____M____,energy=1000)")
    val round3MiniBotResponse = bot.respond("React(entity=10:,time=3,view=____M____,energy=1000,dx=1,dy=-1)")

    (welcomeResponse must_== "") and
    (round2Response must_== "Move(dx=1,dy=-1)|Say(text=Hello)|Spawn(dx=1,dy=-1,name=10:,energy=200)") and
    (round3MasterResponse must_== "Move(dx=1,dy=-1)|Say(text=Hello)|Spawn(dx=1,dy=-1,name=20:,energy=200)") and
    (round3MiniBotResponse must_== "Move(dx=1,dy=-1)|Say(text=Hello)|Explode(size=3)")
  }

  def noMatch = {
    MutableThreadSafeBotContext.reset()
    val bot = new Bot()
    val welcomeResponse = bot.respond("Welcome(name=Test,path=src/test/resources/bottest/nomatch,apocalypse=3,round=1)")
    val round2Response = bot.respond("React(entity=Master,time=2,view=____M____,energy=1000)")

    (welcomeResponse must_== "") and
    (round2Response must_== "")
  }

  def trackedState = {
    MutableThreadSafeBotContext.reset()
    val bot = new Bot()
    val welcomeResponse = bot.respond("Welcome(name=Test,path=src/test/resources/bottest/trackedstate,apocalypse=4,round=1)")
    val round2Response = bot.respond("React(entity=Master,time=2,view=____M____,energy=1000)")
    val round3Response = bot.respond("React(entity=Master,time=3,view=____M____,energy=1000)")
    val round4Response = bot.respond("React(entity=Master,time=4,view=____M____,energy=1000)")

    (welcomeResponse must_== "") and
    (round2Response must_== "Move(dx=1,dy=-1)") and
    (round3Response must_== "Move(dx=1,dy=-1)") and
    (round4Response must_== "Move(dx=1,dy=-1)") and
    (MutableThreadSafeBotContext.trackedState("Master") must_== Map("count" -> "..."))
  }
}

