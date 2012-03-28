package scalatron.botwar.botPlugin.handlers

import scalatron.botwar.botPlugin.protocol._

class RandomMovementHandler extends Handler {
  def react = {
    case cmd: ReactBot => Vector[Action]()
    case cmd: ReactMiniBot => Vector[Action]()
  }
}
