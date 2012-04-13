package scalatron.botwar.toolkit.domain

import org.specs2.Specification

class ViewSpec extends Specification { def is =

  "Specification for View class"                                     ^
                                                                     endp^
  "A View should"                                                    ^
    "be buildable from an encoded string"                            ! build^
    "know the view dimensions"                                       ! dimensions^
    "know the distance viewable in any direction"                    ! distance^
    "treat unknown characters as occluded"                           ! unknown^
                                                                     end

  def build = {
    View(defaultView) must_== View(Vector(
      OccludedByWall, OccludedByWall, EmptyCell, EmptyCell, EmptyCell,
      Wall, Wall, Zugar, EmptyCell, Fluppet,
      EmptyCell, Snorg, MyBot, EmptyCell, Toxifere,
      EmptyCell, EnemyMiniBot, EnemyBot, EmptyCell, EmptyCell,
      MyMiniBot, Snorg, Snorg, Zugar, EmptyCell))
  }

  def dimensions = {
    val view = View(defaultView)
    (view.width must_== 5) and (view.height must_== 5)
  }

  def distance = {
    View(defaultView).maxRange must_== 2
  }

  def unknown = {
    View("zzz") must_== View(Vector(OccludedByWall, OccludedByWall, OccludedByWall))
  }

  private def defaultView = """??___
                              |WWP_B
                              |_bM_p
                              |_sm__
                              |SbbP_""".stripMargin.replaceAll("\n", "")
}

