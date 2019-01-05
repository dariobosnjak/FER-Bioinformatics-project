import java.io.{File, PrintWriter}

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
    * Returns start indices forr all k-length substrings of a string passed as the first parameter.
    *
    * @param str string
    * @param k   substring length
    */
  private def getAllKLengthSubstringStartIndices(X: String, k: Int): MutableHashMap[Long, ArrayBuffer[Int]] = {
    var substringStartIndices = MutableHashMap[Long, ArrayBuffer[Int]]()
    val n = X.length

    for (i <- 0 until n if i + k <= n) {
      if (substringStartIndices.get(X.substring(i, i + k).hashCode).isDefined) {
        substringStartIndices(X.substring(i, i + k).hashCode).append(i)
      } else {
        substringStartIndices.put(X.substring(i, i + k).hashCode, ArrayBuffer[Int]())
        substringStartIndices(X.substring(i, i + k).hashCode).append(i)
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
    val xSubstringStartIndices: MutableHashMap[Long, ArrayBuffer[Int]] = getAllKLengthSubstringStartIndices(X, k)
    println("X gotov", xSubstringStartIndices.keySet.size)

    val kMatchPairs: ArrayBuffer[MatchPair] = ArrayBuffer[MatchPair]()
    for (j <- 0 until m if j + k <= m) {
      val hash = Y.substring(j, j + k).hashCode
      if (xSubstringStartIndices.get(hash).isDefined) {
        // because of hash function it is maybe same substring
        for (xStartIndex <- xSubstringStartIndices(hash)) {
          if (X.substring(xStartIndex, xStartIndex + k) == Y.substring(j, j + k)) {
            // if substrings are the same
            kMatchPairs.append(new MatchPair(new Event(xStartIndex, j, Event.START), new Event(xStartIndex + k, j + k, Event.END)))
          }
        }
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
        dp.put((event.i, event.j), if (event.j == 0) k else k + maxColDp.max(event.j))
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

  def runForFile(filePath: String, k: Int): Unit = {
    // parse FASTA file
    val sequences = FastaReader.parseFastaFile(filePath)
    val X = sequences(0).sequence
    val Y = sequences(1).sequence

    // run LCSk++
    println("file=" + filePath, "k=" + k)
    val similarity = runLcskPlusPlus(X, Y, k)
    println("Similarity: " + similarity)

    // write results
    val file = new File(filePath)
    val fileName = file.getName

    val pw = new PrintWriter(new File("./data/results/scala/k=" + k + "-" + fileName))
    pw.write("Similarity: " + similarity.toString)
    pw.flush()
    pw.close()
  }

  def main(args: Array[String]): Unit = {
    if (args.length != 2) {
      println("ERROR - 2 arguments required: file path, k")
      sys.exit(-1)
    }

    val filePath = args(0)
    val k = args(1).toInt

    val file = new File(filePath)

    if (file.isDirectory) {
      // run for all files in a directory
      for (currentFile <- file.listFiles()) {
        runForFile(currentFile.getAbsolutePath, k)
      }
    } else if (file.isFile) {
      // run for a single file
      runForFile(filePath, k)
    }
  }

}

