package scalatron.botwar.botPlugin

trait CommandParser {

  def parse(command: String): Command = {
    val openBracket = command.indexOf("(")
    val params = (command.substring(openBracket + 1, command.length - 1) split (',') map
                   (_.split('=')) map (e => (e(0) -> e(1)))).toMap
    command.substring(0, openBracket) match {
      case "Welcome" => Welcome(params("name"), params("path"), params("round").toInt)
      case "Goodbye" => Goodbye(params("energy").toInt)
      case "React" if ( params("entity") == "Master" ) => ReactBot(params("entity"), params("time").toInt,
                                                                 params("energy").toInt, View(params("view")))
      case "React" => ReactMiniBot(params("entity"), params("time").toInt, params("energy").toInt,
                                   params("dx").toInt, params("dy").toInt, View(params("view")))
      case _ => throw new IllegalArgumentException("Unknown command: " + command)
    }
  }
}

sealed trait Command
case class Welcome(name: String, path: String, round: Int) extends Command
case class Goodbye(energy: Int) extends Command
case class ReactBot(name: String, time: Int, energy: Int, view: View) extends Command
case class ReactMiniBot(name: String, time: Int, energy: Int, dx: Int, dy: Int, view: View) extends Command
