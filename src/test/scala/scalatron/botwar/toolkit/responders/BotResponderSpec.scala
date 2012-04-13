package scalatron.botwar.toolkit.responders

import org.specs2.Specification
import scalatron.botwar.toolkit.protocol.{Goodbye, Command}

class BotResponderSpec extends Specification { def is =

  "Specification for BotResponder"                                   ^
                                                                     endp^
  "A BotResponder should"                                            ^
    "respond to a recognised command with a new responder"           ! matchCommand^
    "respond to a unrecognised command with itself"                  ! noCommandMatch^
    "respond to an unparsable command with itself"                   ! unknownCommand^
                                                                     end

  class TestBotResponder extends BotResponder {
    def response = "foo"
    protected def forCommand(command: Command) = command match {
      case goodbye: Goodbye => Some(new TestBotResponder())
      case _ => None
    }
  }

  def matchCommand = {
    val responder = new TestBotResponder()
    responder.respond("Goodbye(energy=1000)") must_!= responder
  }

  def noCommandMatch = {
    val responder = new TestBotResponder()
    responder.respond("Welcome(name=Master,path=Foo,apocalypse=5000,round=1)") must_== responder
  }

  def unknownCommand = {
    val responder = new TestBotResponder()
    responder.respond("Foo") must_== responder
  }
}

