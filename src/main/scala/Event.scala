import Event.EventType

/**
  * Class represents an event - a tuple of start or end indices in X and Y strings.
  * Row-major ordering is implemented.
  *
  * @param _i         index in the first string
  * @param _j         index in the second string
  * @param _eventType START or END, if END indices i,j are exclusive
  *
  * @author Dario Bosnjak
  */
class Event(_i: Int, _j: Int, _eventType: EventType) extends Ordered[Event] {
  def i: Int = _i

  def j: Int = _j

  def eventType: EventType = _eventType

  override def equals(obj: Any): Boolean = {
    obj match {
      case that: Event => that.isInstanceOf[Event] && this.hashCode() == that.hashCode()
      case _ => false
    }
  }

  override def hashCode(): Int = {
    val prime = 31
    var result = 1
    result = prime * result + i
    result = prime * result + j
    result = prime * result + eventType.toString.hashCode
    result
  }

  override def compare(that: Event): Int = {
    var cmp = 0
    if (i < that.i || i > that.i) {
      cmp = i.compare(that.i)
    } else {
      // in same row
      if (j < that.j || j > that.j) {
        cmp = j.compare(that.j)
      } else if (j == that.j) {
        // end event is smaller than start event
        if (this.eventType == Event.START && that.eventType == Event.END) {
          cmp = 1
        } else if (that.eventType == Event.START && this.eventType == Event.END) {
          cmp = -1
        } else {
          // same EventType in same row and column
          cmp = 0
        }
      }
    }

    cmp
  }

  override def toString: String = {
    "Pair(" + i + ", " + j + ") " + eventType
  }
}

object Event extends Enumeration {
  type EventType = Value
  val START = Value("START")
  val END = Value("END")


  /**
    * Creates a new instance of the event.
    *
    * @param event event to copy values from
    * @return new instance with same values as the event parameter
    */
  def of(event: Event): Event = {
    new Event(event.i, event.j, event.eventType)
  }
}
