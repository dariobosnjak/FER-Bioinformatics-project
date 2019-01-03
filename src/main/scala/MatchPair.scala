/**
  * Class represents start and end indices of the match pair.
  *
  * @param _startX starting index in the first string, inclusive
  * @param _endX   ending index in the first string, exclusive
  * @param _startY starting index in the second string, inclusive
  * @param _endY   ending index in the second string, exclusive
  */
class MatchPair(_startX: Int, _endX: Int, _startY: Int, _endY: Int) {
  def startX: Int = _startX

  def endX: Int = _endX

  def startY: Int = _startY

  def endY: Int = _endY

  override def toString: String = {
    "start: (" +
      startX + "," + startY + "); " +
      "end: (" + endX + "," + endY + ")"
  }
}