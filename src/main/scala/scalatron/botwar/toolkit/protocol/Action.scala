package scalatron.botwar.toolkit.protocol

sealed trait Action

case class Move(dx: Int, dy: Int) extends Action {
  require(dx >= -1 && dx <= 1)
  require(dy >= -1 && dy <= 1)
  override def toString = "Move(dx=%d,dy=%d)".format(dx, dy)
}

case class Spawn(dx: Int, dy: Int, name: String, energy: Int = 100) extends Action {
  require(dx >= -1 && dx <= 1)
  require(dy >= -1 && dy <= 1)
  require(energy >= 100)
  override def toString = "Spawn(dx=%d,dy=%d,name=%s,energy=%d)".format(dx, dy, name, energy)
}

case class SetName(name: String) extends Action {
  override def toString = "SetName(name=%s)".format(name)
}

case class Explode(size: Int) extends Action {
  require(size > 2 && size < 10)
  override def toString = "Explode(size=%d)".format(size)
}

case class Say(text:String) extends Action {
  override def toString = "Say(text=%s)".format(text)
}

case class Status(text: String) extends Action {
  override def toString = "Status(text=%s)".format(text)
}
