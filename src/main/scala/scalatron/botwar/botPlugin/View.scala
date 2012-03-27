package scalatron.botwar.botPlugin

case class View(entities: IndexedSeq[ViewEntity]) {
  def maxRange: Int = (width - 1) / 2
  def width: Int = scala.math.sqrt(entities.length).toInt
  def height: Int = scala.math.sqrt(entities.length).toInt
}

object View {
  def apply(view: String): View = new View(view.map(ViewEntity.apply))
}

sealed trait ViewEntity
case object OccludedByWall extends ViewEntity
case object EmptyCell extends ViewEntity
case object Wall extends ViewEntity
case object MyBot extends ViewEntity
case object EnemyBot extends ViewEntity
case object MyMiniBot extends ViewEntity
case object EnemyMiniBot extends ViewEntity
case object Zugar extends ViewEntity     // good plant, food
case object Toxifere extends ViewEntity  // bad plant, poisonous
case object Fluppet extends ViewEntity   // good beast, food
case object Snorg extends ViewEntity     // bad beast, predator

object ViewEntity {
  def apply(ch: Char) = ch match {
    case '?' => OccludedByWall
    case '_' => EmptyCell
    case 'W' => Wall
    case 'M' => MyBot
    case 'm' => EnemyBot
    case 'S' => MyMiniBot
    case 's' => EnemyMiniBot
    case 'P' => Zugar
    case 'p' => Toxifere
    case 'B' => Fluppet
    case 'b' => Snorg
    case _ => throw new IllegalArgumentException("Unknown cell content: " + ch)
  }
}
