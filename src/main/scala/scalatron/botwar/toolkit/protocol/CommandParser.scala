package scalatron.botwar.toolkit.protocol

trait CommandParser {

  def parse(command: String): Option[Command] = noneOnException(command, extractParams, extractCommand)

  private def extractParams(command: String) =
    (command.substring(command.indexOf("(") + 1, command.length - 1) split (',') map
                 (_.split('=')) map (e => (e(0) -> e(1)))).toMap

  private def extractCommand(command: String, params: Map[String, String]) = {
    command.substring(0, command.indexOf("(")) match {
      case "Welcome" => Some(Welcome(params("name"), params("path"), params("apocalypse").toInt, params("round").toInt))
      case "Goodbye" => Some(Goodbye(params("energy").toInt))
      case "React" if ( params("entity") == "Master" ) => Some(ReactBot(params("entity"), params("time").toInt,
                                                                        params("energy").toInt, params("view")))
      case "React" => Some(ReactMiniBot(params("entity"), params("time").toInt, params("energy").toInt,
                                        params("dx").toInt, params("dy").toInt, params("view")))
      case _ => None
    }
  }

  private def noneOnException(command: String,
                              fParams: String => Map[String, String],
                              fCommand: (String, Map[String, String]) => Option[Command]): Option[Command] =
    try { fCommand(command, fParams(command)) } catch { case _ => None }
}

sealed trait Command
case class Welcome(name: String, path: String, apocalypse: Int, round: Int) extends Command
case class Goodbye(energy: Int) extends Command
case class ReactBot(name: String, time: Int, energy: Int, view: String) extends Command
case class ReactMiniBot(name: String, time: Int, energy: Int, dx: Int, dy: Int, view: String) extends Command
