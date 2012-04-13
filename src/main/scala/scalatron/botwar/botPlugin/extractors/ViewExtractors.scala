package scalatron.botwar.botPlugin.extractors

import scalatron.botwar.botPlugin.domain.View

object InDanger {
  def unapply(view: View): Option[View] = Some(view)
}
