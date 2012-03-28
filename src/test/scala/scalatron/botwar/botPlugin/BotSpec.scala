package scalatron.botwar.botPlugin

import org.specs2.Specification
import protocol._

class BotSpec extends Specification { def is =

  "Specification for Bot"                                            ^
                                                                     endp^
  "A Bot should"                                                     ^
    "pass the command to all installed strategies"                   ! respond^
    "return no action if no strategies match the action"             ! noMatch^
                                                                     end

  def respond = {
    val bot = new Bot()
    bot.installStrategies(strategy1 :: strategy2 :: strategy3 :: Nil)
    bot.respond("React(entity=Master,time=1,energy=500,view=____M____)") must_==
      "Move(dx=1,dy=0)|Status(text=foo)"
  }

  def noMatch = {
    val bot = new Bot()
    bot.installStrategies(strategy1 :: strategy2 :: strategy3 :: Nil)
    bot.respond("React(entity=Foo,time=1,energy=500,view=____M____,dx=10,dy=0)") must_== ""
  }

  def strategy1: PartialFunction[Command, IndexedSeq[Action]] = {
    case Welcome(_, _, _) => Vector[Action]()
  }

  def strategy2: PartialFunction[Command, IndexedSeq[Action]] = {
    case ReactBot("Master", _, _, _) => Vector[Action](Move(1, 0), Status("foo"))
  }

  def strategy3: PartialFunction[Command, IndexedSeq[Action]] = {
    case Goodbye(_) => Vector[Action]()
  }
}

