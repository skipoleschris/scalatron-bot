package scalatron.botwar.botPlugin.handlers

import scalatron.botwar.botPlugin.protocol._

class ConfigurationHandler extends Handler {
  def react = {
    case Welcome(_, path, _) => Vector[Action]()
  }
}
