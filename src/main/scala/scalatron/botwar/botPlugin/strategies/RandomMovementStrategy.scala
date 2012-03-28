package scalatron.botwar.botPlugin.strategies

import scalatron.botwar.botPlugin.protocol._
import util.Random

class RandomMovementStrategy extends Strategy {
  def react = {
    case cmd: ReactBot => Vector[Action](moveRandom)
    case cmd: ReactMiniBot => Vector[Action](moveRandom)
  }

  private val random = new Random()

  private def moveRandom = Move(random.nextInt(3) - 1, random.nextInt(3) - 1)
}
