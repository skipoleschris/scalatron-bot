package scalatron.botwar.botPlugin.domain

case class View(entities: IndexedSeq[ViewEntity]) {
  def maxRange: Int = (width - 1) / 2
  def width: Int = scala.math.sqrt(entities.length).toInt
  def height: Int = scala.math.sqrt(entities.length).toInt
}

object View {
  def apply(view: String): View = new View(view.map(ViewEntity.apply))
}

