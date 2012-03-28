package scalatron.botwar.botPlugin.handlers

import org.specs2.Specification
import scalatron.botwar.botPlugin.protocol._

class HandlerChainSpec extends Specification { def is =

  "Specification for the Handler Chain"                              ^
                                                                     endp ^
  "A Handler Chain should"                                           ^
    "allow new handlers to be installed"                             ! installHandlers^
    "find a handler for a given command"                             ! findHandler^
    "return None if no handler can be found for a command"           ! noSuitableHandler^
                                                                     end

  class TestHandlerChain extends HandlerChain

  def installHandlers = {
    val chain = new TestHandlerChain()
    chain.installHandlers(handler1 :: handler3 :: Nil)

    chain.installedHandlers must_== (handler1 :: handler3 :: Nil)
  }

  def findHandler = {
    val chain = new TestHandlerChain()
    chain.installHandlers(handler1 :: handler3 :: Nil)

    chain.forCommand(Welcome("test", "test", 1)) must_== Some(handler1)
  }

  def noSuitableHandler = {
    val chain = new TestHandlerChain()
    chain.installHandlers(handler1 :: handler3 :: Nil)

    chain.forCommand(ReactBot("Foo", 1, 100, View("____M____"))) must_== None
  }

  val handler1: PartialFunction[Command, IndexedSeq[Action]] = {
    case Welcome(_, _, _) => Vector[Action]()
  }

  val handler2: PartialFunction[Command, IndexedSeq[Action]] = {
    case ReactBot("Master", _, _, _) => Vector[Action](Move(1, 0), Status("foo"))
  }

  val handler3: PartialFunction[Command, IndexedSeq[Action]] = {
    case Goodbye(_) => Vector[Action]()
  }
}

