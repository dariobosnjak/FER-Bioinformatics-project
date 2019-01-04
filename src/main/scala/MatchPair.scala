/**
  * Class represents start and end indices of the match pair.
  *
  * @param _startEvent an event that defines the start of the match pair
  * @param _endEvent   an event that defines the end of the match pair
  */
class MatchPair(_startEvent: Event, _endEvent: Event) {
  def startEvent: Event = _startEvent

  def endEvent: Event = _endEvent


  /**
    * Returns true if this match pair continues that match pair.
    * Match pair P continues match pair G if starting indices of G (iG, jG) one are on the same primary diagonal as (iP, jP), and
    * iP - iG = 1.
    *
    * @param that other match pair
    * @return true if this continues that
    */
  def continues(that: MatchPair): Boolean = {
    // on same primary diagonal
    val samePrimDiag: Boolean = this.startEvent.i - this.startEvent.j == that.startEvent.i - that.startEvent.j
    // this match pair is only one down-right position from that match pair
    val oneDownRightPosition: Boolean = (this.startEvent.i - that.startEvent.i) == 1

    samePrimDiag && oneDownRightPosition
  }

  override def toString: String = {
    "start: (" +
      startEvent.i + "," + startEvent.j + "); " +
      "end: (" + endEvent.i + "," + endEvent.j + ")"
  }
}
