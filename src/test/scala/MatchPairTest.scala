import org.scalatest.FlatSpec

/**
  * MachPair test class
  * Dario Bosnjak
  */
class MatchPairTest extends FlatSpec {
  "A MatchPair P" must "continue MatchPair G" in {
    val k = 3
    val pStart = new Event(4, 3, Event.START)
    val pEnd = new Event(4 + k - 1, 3 + k - 1, Event.END)
    val p = new MatchPair(pStart, pEnd)

    val gStart = new Event(3, 2, Event.START)
    val gEnd = new Event(3 + k - 1, 2 + k - 1, Event.END)
    val g = new MatchPair(gStart, gEnd)

    assert(p.continues(g))
  }

  "A MatchPair G" must "NOT continue MatchPair P" in {
    val k = 3
    val pStart = new Event(4, 3, Event.START)
    val pEnd = new Event(4 + k - 1, 3 + k - 1, Event.END)
    val p = new MatchPair(pStart, pEnd)

    val gStart = new Event(3, 2, Event.START)
    val gEnd = new Event(3 + k - 1, 2 + k - 1, Event.END)
    val g = new MatchPair(gStart, gEnd)

    assert(!g.continues(p))
  }

  "A MatchPair P" must "NOT continue MatchPair G" in {
    val k = 3
    val pStart = new Event(4, 3, Event.START)
    val pEnd = new Event(4 + k - 1, 3 + k - 1, Event.END)
    val p = new MatchPair(pStart, pEnd)

    val gStart = new Event(0, 0, Event.START)
    val gEnd = new Event(3, 2, Event.END)
    val g = new MatchPair(gStart, gEnd)

    assert(!p.continues(g))
  }
}
