package scalatron.botwar.toolkit.responders

import org.specs2.Specification

class InitialisingResponderSpec extends Specification { def is =

  "Specification for InitialisingResponder"                          ^
                                                                     endp^
  "A InitialisingResponder should"                                   ^
    "always return empty response message"                           ! emptyResponseMessage^
    "ignore non Welcome commands"                                    ! nonWelcomeCommand^
    "should return a response with env when sent Welcome command"    ! welcomeCommand^
                                                                     end


  def emptyResponseMessage = {
    val response = InitialisingResponder(TestBotContext()).respond("Welcome(name=Test,path=src/test/resources,apocalypse=5000,round=1)")
    response.response must_== ""
  }

  def nonWelcomeCommand = {
    val response = InitialisingResponder(TestBotContext()).respond("Goodbye(energy=1000)")
    response must_== Response.empty
  }

  def welcomeCommand = {
    val response = InitialisingResponder(TestBotContext()).respond("Welcome(name=Test,path=src/test/resources,apocalypse=5000,round=1)")

    (response.actions must beEmpty) and
    (response.trackedState must beEmpty) and
    (response.environment.get._1.strategyGroups must_== Vector("movement" -> ("RandomMovementStrategy" :: Nil))) and
    (response.environment.get._2 must haveSize(1)) and
    (response.environment.get._2.head must haveSize(1))
  }
}

