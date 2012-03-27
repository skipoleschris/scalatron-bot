package scalatron.botwar.botPlugin

class ControlFunctionFactory {
  def create: (String => String) = new Bot().respond _
}

class Bot extends CommandParser {

  type Handler = PartialFunction[Command, IndexedSeq[Action]]
  private val handlers = List[Handler]()

  def respond(input: String): String = {
    val actions = for (
      command <- parse(input);
      handler <- handlers find (_.isDefinedAt(command))
    ) yield handler.apply(command)

    actions map (_.mkString("|")) getOrElse  ""
  }
}
