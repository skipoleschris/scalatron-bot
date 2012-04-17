package scalatron.botwar.toolkit.responders

import org.specs2.Specification
import scalatron.botwar.toolkit.protocol.{Say, Goodbye, Command}

class BotResponderSpec extends Specification { def is =

  "Specification for BotResponder"                                   ^
                                                                     endp^
  "A BotResponder should"                                            ^
    "respond to a recognised command with a response"                ! matchCommand^
    "respond to a unrecognised command with empty response"          ! noCommandMatch^
    "respond to an unparsable command with empty response"           ! unknownCommand^
                                                                     endp^
  "Creating a new BotResponder should"                               ^
    "return an initialising responder for uninitialised context"     ! uninitialised^
    "return a bot control response for an ininitialised context"     ! initialised^
                                                                     end

  class TestBotResponder extends BotResponder {
    def response = "foo"
    protected def forCommand(command: Command) = command match {
      case goodbye: Goodbye => Some(Response(IndexedSeq(Say("Goodbye"))))
      case _ => None
    }
  }

  def matchCommand = {
    val responder = new TestBotResponder()
    responder.respond("Goodbye(energy=1000)") must_== Response(IndexedSeq(Say("Goodbye")))
  }

  def noCommandMatch = {
    val responder = new TestBotResponder()
    responder.respond("Welcome(name=Master,path=Foo,apocalypse=5000,round=1)") must_== Response.empty
  }

  def unknownCommand = {
    val responder = new TestBotResponder()
    responder.respond("Foo") must_== Response.empty
  }

  def uninitialised = {
    BotResponder(TestBotContext(initialised = false), Stream.from(1)) must beAnInstanceOf[InitialisingResponder]
  }

  def initialised = {
    BotResponder(TestBotContext(), Stream.from(1)) must beAnInstanceOf[BotControlResponder]
  }
}

