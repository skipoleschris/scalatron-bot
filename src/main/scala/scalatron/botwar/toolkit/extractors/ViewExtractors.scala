package scalatron.botwar.toolkit.extractors

import scalatron.botwar.toolkit.domain.View

object InDanger {
  def unapply(view: View): Option[View] = Some(view)
}
