package scalatron.botwar.strategies

import util.Random
import scalatron.botwar.toolkit.configuration.BotConfig
import scalatron.botwar.toolkit.domain._
import scalatron.botwar.toolkit.strategies.Strategy

class RandomMovementStrategy extends Strategy {
  def react(config: BotConfig) = {
    case _ => Set(moveRandom)
  }

  private val random = new Random()

  private def moveRandom = MoveOutcome(DeltaOffset(random.nextInt(3) - 1, random.nextInt(3) - 1))
}
