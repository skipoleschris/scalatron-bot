package scalatron.botwar.toolkit.domain

import org.specs2.Specification
import collection.immutable.TreeMap

class MiniBotNameEncodeDecodeSpec extends Specification {def is =

  "Specification for MiniBotNameEncodeDecode"                        ^
                                                                     endp^
  "A Minibot Name Encoder/Decoder should"                            ^
    "generate a new name and encode the supplied state"              ! newNameWithState^
    "generate a new name when there in no state"                     ! newNameNoState^
    "encode a name and supplied state"                               ! encodeWithState^
    "encode a name when there is no state"                           ! encodeNoState^
    "decode a name and state"                                        ! decodeWithState^
    "decode a name with no state present"                            ! decodeNoState^
                                                                     end

  def newNameWithState = {
    val identity = MiniBotNameEncodeDecode.newName(Stream.from(1), TreeMap(("foo" -> "FOO"), ("bar" -> "BAR")))

    (identity._1 must_== "1") and
    (identity._2 must_== "1:bar/BAR;foo/FOO") and
    (identity._3.head must_== 2)
  }

  def newNameNoState = {
    val identity = MiniBotNameEncodeDecode.newName(Stream.from(1), Map.empty)

    (identity._1 must_== "1") and
    (identity._2 must_== "1:") and
    (identity._3.head must_== 2)
  }

  def encodeWithState = {
    val encoded = MiniBotNameEncodeDecode.encode("1", TreeMap(("foo" -> "FOO"), ("bar" -> "BAR")))
    encoded must_== "1:bar/BAR;foo/FOO"
  }

  def encodeNoState = {
    val encoded = MiniBotNameEncodeDecode.encode("1", Map.empty)
    encoded must_== "1:"
  }

  def decodeWithState = {
    val decoded = MiniBotNameEncodeDecode.decode("1:bar/BAR;foo/FOO")

    (decoded._1 must_== "1") and
    (decoded._2 must havePairs(("bar" -> "BAR"), ("foo" -> "FOO")))
  }

  def decodeNoState = {
    val decoded = MiniBotNameEncodeDecode.decode("1:")

    (decoded._1 must_== "1") and
    (decoded._2 must beEmpty)
  }
}

