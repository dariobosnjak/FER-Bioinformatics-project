import java.io.File

import scala.collection.Set
import scala.collection.mutable.{ArrayBuffer, HashMap => MutableHashMap}
import scala.io.Source

object LCSkPlusPlus {

  /**
    * Returns true if the pair that starts on (i, j) is a match pair.
    * (i, j) is the start of a match pair if X(i+f) == Y(j+f), for f=[0, k-1]
    *
    * @param i starting index in X
    * @param j starting index in Y
    * @return true if the (i, j) is a match pair, false otherwise
    */
  def isKMatchPair(X: String, Y: String, i: Int, j: Int, k: Int): Boolean = {
    var result = true
    // conditioned for loop - on first difference break the loop
    for (f <- 0 until k if result) {
      if (X(f) != Y(f)) {
        result = false
      }
    }
    result
  }

  /**
    * Finds k match pairs defined by starting indices (i, j) and length k
    * For small k a hash table can be used for finding k match pairs in O(n + m + r) time.
    *
    * @return array of Int tuples - starting indices in X and Y of each match pair
    */
  def findKMatchPairs(X: String, Y: String, k: Int): Array[MatchPair] = {
    val n = X.length
    val m = Y.length

    // key = substring
    // value = an array of starting indices
    val xSubstringStartIndices = MutableHashMap[String, ArrayBuffer[Int]]()
    val ySubstringStartIndices = MutableHashMap[String, ArrayBuffer[Int]]()

    // hash all k-length substrings of X and Y in O(n + m) time
    for (i <- 0 until n if i + k <= n) {
      val substring = X.substring(i, i + k)
      val currentValue = xSubstringStartIndices.getOrElse(substring, ArrayBuffer[Int]())
      currentValue.append(i)
      xSubstringStartIndices.update(substring, currentValue)
    }
    for (j <- 0 until m if j + k  <= m) {
      val substring = Y.substring(j, j + k)
      val currentValue = ySubstringStartIndices.getOrElse(substring, ArrayBuffer[Int]())
      currentValue.append(j)
      ySubstringStartIndices.update(substring, currentValue)
    }

    // get match pairs in O(r) time, where r is number of match pairs
    val kMatchPairs: ArrayBuffer[MatchPair] = ArrayBuffer[MatchPair]()
    // if both strings contain substring it is a match pair
    val substrings: Set[String] = xSubstringStartIndices.keySet.intersect(ySubstringStartIndices.keySet)
    for (substring <- substrings) {
      val xStartIndices: ArrayBuffer[Int] = xSubstringStartIndices(substring)
      val yStartIndices: ArrayBuffer[Int] = ySubstringStartIndices(substring)
      // create match pairs - combine all starting indices from X with all from Y
      for (i <- xStartIndices; j <- yStartIndices) {
        // end is exclusive
        kMatchPairs.append(new MatchPair(new Event(i, j, Event.START), new Event(i + k, j + k, Event.END)))
      }
    }
    kMatchPairs.toArray
  }

  /**
    * Returns start and end events from match pairs.
    *
    * @param matchPairs match pairs to process
    * @return start and end events
    */
  def getEvents(matchPairs: Array[MatchPair]): Array[Event] = {
    val events: ArrayBuffer[Event] = ArrayBuffer[Event]()
    for (matchPair <- matchPairs) {
      events.append(
        Event.of(matchPair.startEvent),
        Event.of(matchPair.endEvent))
    }
    events.toArray
  }

  /**
    * Returns an event from the array of events for which it is true: an event passed as the second parameter continues
    * that event.
    *
    * @param events array of events to check continuation of match pairs
    * @param event  event that continues some event from events array
    * @return Option[Event] - if event is found it is defined, otherwise it is empty
    */
  def getEventThatContinues(events: Array[Event], event: Event): Option[Event] = {
    var notFound = true
    var g: Event = new Event(event.i - 1, event.j - 1, Event.START)

    // O(logn)
    var left = 0
    var right = events.length - 1
    while (left <= right && notFound) {
      val middle = (right + left) / 2
      if (events(middle) < g) {
        // go to the right half
        left = middle + 1
      } else if (events(middle) > g) {
        right = middle - 1
      } else {
        notFound = false
      }
    }

    if (notFound) Option[Event](null) else Option[Event](g)
  }

  def runLcskPlusPlus(X: String, Y: String, k: Int): Int = {
    val n = X.length
    val m = Y.length

    val dp: Array[Array[Int]] = Array.ofDim(n, m)
    val maxColDp: Array[Int] = Array.fill(m) {
      0
    }

    val matchPairs: Array[MatchPair] = findKMatchPairs(X, Y, k)
    val events: Array[Event] = getEvents(matchPairs).sorted

    for (event <- events) {
      if (event.eventType == Event.START) {
        dp(event.i)(event.j) = if (maxColDp.slice(0, event.j + 1 - 1).isEmpty) k else k + maxColDp.slice(0, event.j + 1 - 1).max

      }
      else if (event.eventType == Event.END) {
        // calculate start event
        val p = new Event(event.i - k, event.j - k, Event.START)
        // END event contains exclusive indices - when indexing dp and maxColDp use i-1 and j-1
        val g: Option[Event] = getEventThatContinues(events, p)
        if (g.isDefined) {
          dp(p.i)(p.j) = math.max(dp(p.i)(p.j), dp(g.get.i)(g.get.j) + 1)
        }
        maxColDp(event.j - 1) = math.max(maxColDp(event.j - 1), dp(p.i)(p.j))
      }
    }

    dp.flatten.max
  }

  def main(args: Array[String]): Unit = {
    val filePath = args(0)
    val k = args(1).toInt

    var lineIterator = Source.fromFile(filePath).getLines()
    if (lineIterator.size != 2) {
      println("ERROR - FILE SHOULD CONTAIN 2 LINES")
      sys.exit(-1)
    }
    lineIterator = Source.fromFile(filePath).getLines()
    val X: String = lineIterator.next()
    val Y: String = lineIterator.next()

    println("X=" + X, "Y=" + Y, "k=" + k)

    val similarity = runLcskPlusPlus(X, Y, k)
    println(similarity)
  }
}

