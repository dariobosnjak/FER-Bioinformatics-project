import scala.collection.Set
import scala.collection.mutable.{ArrayBuffer, HashMap => MutableHashMap}

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
    for (i <- 0 until n if i + k - 1 <= n - 1) {
      val substring = X.substring(i, i + k)
      val currentValue = xSubstringStartIndices.getOrElse(substring, ArrayBuffer[Int]())
      currentValue.append(i)
      xSubstringStartIndices.update(substring, currentValue)
    }
    for (j <- 0 until m if j + k - 1 <= m - 1) {
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
        kMatchPairs.append(new MatchPair(i, i + k - 1, j, j + k - 1))
      }
    }
    kMatchPairs.toArray
  }

  def getEvents(matchPairs: Array[MatchPair]): Array[Event] = {
    val events: ArrayBuffer[Event] = ArrayBuffer[Event]()
    for (matchPair <- matchPairs) {
      events.append(
        new Event(matchPair.startX, matchPair.startY, Event.START),
        new Event(matchPair.endX, matchPair.endY, Event.END))
    }
    events.toArray
  }

  def findEventThatContinues(events: Array[Event], event: Event): Option[Event] = {
    var notFound = true
    var g: Option[Event] = Option[Event](null)
    for (e <- events; if notFound) {
      if (event.continues(e)) {
        notFound = false
        g = Option[Event](Event.of(e))
      }
    }
    g
  }

  def main(args: Array[String]): Unit = {
    val X = args(0)
    val Y = args(1)
    val k = args(2).toInt

    val n = X.length
    val m = Y.length

    val maxColDp: Array[Int] = Array.fill(n){0}

    val matchPairs: Array[MatchPair] = findKMatchPairs(X, Y, k)
    println(matchPairs.toList)

    val events: Array[Event] = getEvents(matchPairs)

    var dp: Array[Array[Int]] = Array.ofDim(n, m)

    for (event <- events) {
      if (event.eventType == Event.START) {
        dp(event.i)(event.j) = k + maxColDp.slice(0, event.j + 1).max // TODO - u sliceu mora ici + 1 jer za j=0 vrati Empty?
      } else if (event.eventType == Event.END) {
        val g: Option[Event] = findEventThatContinues(events, event)
        if (g.isDefined) {
          dp(event.i)(event.j) = math.max(dp(event.i)(event.j), dp(g.get.i)(g.get.j) + 1)
        }
        maxColDp(event.j + k - 1) = math.max(maxColDp(event.j + k - 1), dp(event.i)(event.j)) // TODO - mora ici k-1 zbog 0-based indexa?
      }
    }

    val result = dp.flatten.max

    println(result)

    val e1 = new Event(2,8,Event.START)
    val e2 = new Event(3,9,Event.START)
    println(e2.continues(e1))
    println(e1.continues(e2))
  }
}

