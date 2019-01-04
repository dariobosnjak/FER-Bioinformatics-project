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
    * Returns start indices forr all k-length substrings of a string passed as the first parameter.
    *
    * @param str string
    * @param k   substring length
    */
  private def getAllKLengthSubstringStartIndices(str: String, k: Int, currentSubstringStartIndices: Option[MutableHashMap[String, ArrayBuffer[Int]]]): MutableHashMap[String, ArrayBuffer[Int]] = {
    var substringStartIndices = MutableHashMap[String, ArrayBuffer[Int]]()
    val n = str.length

    for (i <- 0 until n if i + k <= n) {
      // add only substrings that are in both strings
      if (currentSubstringStartIndices.isEmpty ||
        (currentSubstringStartIndices.isDefined && currentSubstringStartIndices.get.get(str.substring(i, i + k)).isDefined)) {
        if (substringStartIndices.get(str.substring(i, i + k)).isDefined) {
          substringStartIndices(str.substring(i, i + k)).append(i)
        } else {
          substringStartIndices.put(str.substring(i, i + k), ArrayBuffer[Int]())
          substringStartIndices(str.substring(i, i + k)).append(i)
        }
      }
    }
    substringStartIndices
  }

  /**
    * Returns k match pairs defined by starting indices (i, j) and length k
    * For small k a hash table can be used for finding k match pairs in O(n + m + r) time.
    *
    * @return array of Int tuples - starting indices in X and Y of each match pair
    */
  def getKMatchPairs(X: String, Y: String, k: Int): Array[MatchPair] = {
    val n = X.length
    val m = Y.length
    val freeMem = java.lang.Runtime.getRuntime
    // hash all k-length substrings of X and Y in O(n + m) time
    // key = substring
    // value = an array of starting indices
    val xSubstringStartIndices: MutableHashMap[String, ArrayBuffer[Int]] = getAllKLengthSubstringStartIndices(X, k, Option[MutableHashMap[String, ArrayBuffer[Int]]](null))
    println("X gotov")
    val ySubstringStartIndices: MutableHashMap[String, ArrayBuffer[Int]] = getAllKLengthSubstringStartIndices(Y, k, Option[MutableHashMap[String, ArrayBuffer[Int]]](xSubstringStartIndices))
    println("Y gotov")

    // get match pairs in O(r) time, where r is number of match pairs
    val kMatchPairs: ArrayBuffer[MatchPair] = ArrayBuffer[MatchPair]()
    // if both strings contain substring it is a match pair
    val substrings: Set[String] = xSubstringStartIndices.keySet.intersect(ySubstringStartIndices.keySet)
    for (substring <- substrings) {
      // create match pairs - combine all starting indices from X with all from Y
      for (i <- xSubstringStartIndices(substring); j <- ySubstringStartIndices(substring)) {
        // end is exclusive -> (i+k, j+k)
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
        matchPair.startEvent,
        matchPair.endEvent
      )
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

    // due to large input strings we use hash map to represent sparse matrix
    val dp: MutableHashMap[(Int, Int), Int] = MutableHashMap[(Int, Int), Int]()
    val maxColDp = new FenwickTree(m)

    val matchPairs: Array[MatchPair] = getKMatchPairs(X, Y, k)
    println("match parovi pronadjeni")
    val events: Array[Event] = getEvents(matchPairs).sorted
    println("eventi pronadjeni")
    println("nEvents: ", events.length)
    for (event <- events) {
      if (event.eventType == Event.START) {
        dp.put((event.i, event.j), if (event.j - 0 == 0) k else k + maxColDp.max(event.j))
      }
      else if (event.eventType == Event.END) {
        // calculate start event
        val p = new Event(event.i - k, event.j - k, Event.START)
        val g: Option[Event] = getEventThatContinues(events, p)
        if (g.isDefined) {
          dp.put((p.i, p.j), math.max(dp((p.i, p.j)), dp((g.get.i, g.get.j)) + 1))
        }
        // END event contains exclusive indices - when indexing dp and maxColDp use i-1 and j-1
        maxColDp.update(event.j - 1, math.max(maxColDp.tree(event.j - 1)._1, dp((p.i, p.j))), p)
      }
    }

    if (dp.values.size > 0)
      dp.values.max
    else 0
  }

  def main(args: Array[String]): Unit = {
    if (args.length != 2) {
      println("ERROR - 2 arguments required: file path, k")
      sys.exit(-1)
    }

    val filePath = args(0)
    val k = args(1).toInt

    var lineIterator = Source.fromFile(filePath).getLines()
    if (lineIterator.size != 2) {
      println("ERROR - file should contain 2 lines")
      sys.exit(-1)
    }
    lineIterator = Source.fromFile(filePath).getLines()
    val X: String = lineIterator.next()
    val Y: String = lineIterator.next()

    println("file=" + filePath, "k=" + k)
    println("file=" + filePath, "k=" + k)
    val similarity = runLcskPlusPlus(X, Y, k)
    println("Similarity: " + similarity)

  }
}

