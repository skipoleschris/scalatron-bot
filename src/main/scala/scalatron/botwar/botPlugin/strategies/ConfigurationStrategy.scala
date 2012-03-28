package scalatron.botwar.botPlugin.strategies

import scalatron.botwar.botPlugin.protocol._

class ConfigurationStrategy extends Strategy {
  def react = {
    case Welcome(_, path, _) => Vector[Action]()
  }
}
