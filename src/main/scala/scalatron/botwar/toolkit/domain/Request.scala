package scalatron.botwar.toolkit.domain

case class Request(context: Context, state: State)
case class Context(name: String, time: Int, energy: Int, view: View, offset: Option[DeltaOffset])

object Request {
  def apply(name: String,
            time: Int,
            energy: Int,
            view: String,
            offset: Option[DeltaOffset],
            trackedState: (String) => Map[String, String]): Request = {
    val nameAndRunningState = if ( name == "Master" ) (name -> Map[String, String]())
                              else MiniBotNameEncodeDecode.decode(name)

    val context = Context(nameAndRunningState._1, time, energy, View(view), offset)
    val state = State(nameAndRunningState._2, trackedState(nameAndRunningState._1))
    Request(context, state)
  }
}
