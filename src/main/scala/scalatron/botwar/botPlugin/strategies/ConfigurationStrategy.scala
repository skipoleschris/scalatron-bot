package scalatron.botwar.botPlugin.strategies

import scalatron.botwar.botPlugin.protocol._

class ConfigurationStrategy {
  def react: PartialFunction[Command, IndexedSeq[Action]] = {
    case Welcome(_, path, apocalypse, round) => Vector[Action]()
  }
}
