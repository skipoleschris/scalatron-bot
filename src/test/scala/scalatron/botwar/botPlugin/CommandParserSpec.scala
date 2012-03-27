package scalatron.botwar.botPlugin

import org.specs2.Specification

class CommandParserSpec extends Specification with CommandParser { def is =

  "Specification for the command parser"                             ^
                                                                     endp^
  "A Command Parser should"                                          ^
    "parse the Welcome command"                                      ! welcome^
    "parse the React command for a bot"                              ! reactBot^
    "parse the React command for a mini-bot"                         ! reactMiniBot^
    "parse the Goodbye command"                                      ! goodbye^
                                                                     end

  def welcome = {
    parse("Welcome(name=Test,path=/test,round=1)") must_== Welcome("Test", "/test", 1)
  }

  def reactBot = {
    val reactTo = parse("React(entity=Master,time=100,view=____M____,energy=1000)")
    reactTo must_== ReactBot("Master", 100, 1000, simpleView)
  }

  def reactMiniBot = {
    val reactTo = parse("React(entity=Mini,time=100,view=____M____,energy=1000,dx=-10,dy=3)")
    reactTo must_== ReactMiniBot("Mini", 100, 1000, -10, 3, simpleView)
  }

  def goodbye = {
    parse("Goodbye(energy=1000)") must_== Goodbye(1000)
  }

  def simpleView = View(Vector(EmptyCell, EmptyCell, EmptyCell, EmptyCell, MyBot, EmptyCell, EmptyCell, EmptyCell, EmptyCell))
}

