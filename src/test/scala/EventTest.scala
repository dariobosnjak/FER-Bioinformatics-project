import org.scalatest.FlatSpec

/**
  * Event test class
  * Dario Bosnjak
  */
class EventTest extends FlatSpec{
  "An Event (1,2)" must "be smaller than Event (1,3)" in {
    val p = new Event(1,2, Event.START)
    val g = new Event(1,3, Event.START)

    assert(p < g)
  }

  "An Event (1,2, END)" must "be smaller than Event (1,3, START)" in {
    val p = new Event(1,2, Event.END)
    val g = new Event(1,2, Event.START)

    assert(p < g)
  }

  "An Event (0,5)" must "be smaller than Event (1,3)" in {
    val p = new Event(0,5, Event.START)
    val g = new Event(1,3, Event.END)

    assert(p < g)
  }

  "An Event (1, 1, START)" must "be equal to Event (1,1, START)" in {
    val p = new Event(1,1, Event.START)
    val g = new Event(1,1, Event.START)

    assert(p == g)
  }

}
