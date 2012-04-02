package scalatron.botwar.botPlugin.strategies

import util.Random
import scalatron.botwar.botPlugin.configuration.BotConfig
import scalatron.botwar.botPlugin.domain._

class RandomMovementStrategy extends Strategy {
  def react(config: BotConfig) = {
    case _ => Set(moveRandom)
  }

  private val random = new Random()
  private def moveRandom = MoveOutcome(DeltaOffset(random.nextInt(3) - 1, random.nextInt(3) - 1))
}
