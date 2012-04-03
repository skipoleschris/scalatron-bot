package scalatron.botwar.botPlugin.domain

object MiniBotNameEncodeDecode {

  def newName(sequenceGenerator: Stream[Int], state: Map[String, String]) =
    ("%s:%s".format(sequenceGenerator.head, encodeState(state)), sequenceGenerator.tail)

  def encode(name: String, state: Map[String, String]) = "%s:%s".format(name, encodeState(state))

  def decode(encoded: String): (String, Map[String, String]) = {
    val colonIndex = encoded indexOf ":"
    val params = encoded substring (colonIndex + 1) split
                   (";") map (_ split "/") map
                   (item => (item(0), item(1)))
    (encoded.substring(0, colonIndex), Map(params : _*))
  }

  private def encodeState(state: Map[String, String]) =
    (state map (item => "%s/%s".format(item._1, item._2))).mkString(";")
}
