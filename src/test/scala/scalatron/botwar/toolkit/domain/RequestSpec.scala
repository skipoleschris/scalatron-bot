package scalatron.botwar.toolkit.domain

import org.specs2.Specification

class RequestSpec extends Specification { def is =

  "Specification for Request"                                        ^
                                                                     endp^
  "A Request should"                                                 ^
    "be creatable from a starting set of data"                       ! create^
    "be creatable with existing tracked state"                       ! createWithState^
                                                                     end

  def create = {
    val request = Request("Master", 1, 1000, "____M____", None, _ => Map.empty)

    (request.context.name must_== "Master") and
    (request.context.time must_== 1) and
    (request.context.energy must_== 1000) and
    (request.context.offset must_== None) and
    (request.context.view must_== View("____M____")) and
    (request.state.running must beEmpty) and
    (request.state.tracked must beEmpty)
  }

  def createWithState = {
    def trackedState(name: String): Map[String, String] = name match {
      case "Master" => Map("foo" -> "FOO")
      case _ => Map.empty
    }
    val request = Request("Master", 1, 1000, "____M____", None, trackedState)

    (request.state.running must beEmpty) and
    (request.state.tracked must_== Map("foo" -> "FOO"))
  }
}

