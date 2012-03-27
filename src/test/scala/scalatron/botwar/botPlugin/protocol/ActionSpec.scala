package scalatron.botwar.botPlugin.protocol

import org.specs2.Specification

class ActionSpec extends Specification { def is =

  "Specification for Actions"                                        ^
                                                                     endp^
  "An Action should"                                                 ^
    "produce the Move action"                                        ! move^
    "produce the Spawn action"                                       ! spawn^
    "produce the Spawn action with default energy"                   ! spawnWithDefaultEnergy^
    "produce the SetName action"                                     ! setName^
    "produce the Explode action"                                     ! explode^
    "produce the Say action"                                         ! say^
    "produce the Status action"                                      ! status^
                                                                     end

  def move = {
    Move(-1, 0).toString must_== "Move(dx=-1,dy=0)"
  }

  def spawn = {
    Spawn(-1, 0, "Foo", 250).toString must_== "Spawn(dx=-1,dy=0,name=Foo,energy=250)"
  }

  def spawnWithDefaultEnergy = {
    Spawn(-1, 0, "Foo").toString must_== "Spawn(dx=-1,dy=0,name=Foo,energy=100)"
  }

  def setName = {
    SetName("Foo").toString must_== "SetName(name=Foo)"
  }

  def explode = {
    Explode(5).toString must_== "Explode(size=5)"
  }

  def say = {
    Say("Hello World").toString must_== "Say(text=Hello World)"
  }

  def status = {
    Status("Hello World").toString must_== "Status(text=Hello World)"
  }
}

