package scalatron.botwar.toolkit.responders

import org.specs2.Specification

class InitialisingResponderSpec extends Specification { def is =

  "Specification for InitialisingResponder"                          ^
                                                                     endp^
  "A InitialisingResponder should"                                   ^
    "always return an empty response message"                        ! emptyResponseMessage^
    "ignore non Welcome commands"                                    ! nonWelcomeCommand^
    "should return a BotControlResponder when sent Welcome command"  ! welcomeCommand^
                                                                     end

  def emptyResponseMessage = {
    InitialisingResponder.response must_== ""
  }

  def nonWelcomeCommand = {
    InitialisingResponder.respond("Goodbye(energy=1000)") must_== InitialisingResponder
  }

  def welcomeCommand = {
    val responder = InitialisingResponder.respond("Welcome(name=Test,path=src/test/resources,apocalypse=5000,round=1)").asInstanceOf[BotControlResponder]

    (responder.lastActions must beEmpty) and
    (responder.environment.botConfig.strategyGroups must_== Vector("movement" -> ("RandomMovementStrategy" :: Nil))) and
    (responder.environment.strategies must haveSize(1)) and
    (responder.environment.strategies.head must haveSize(1)) and
    (responder.environment.trackedState must_== Map.empty[String, Map[String, String]]) and
    (responder.environment.sequenceGenerator.head must_== 1)
  }
}

