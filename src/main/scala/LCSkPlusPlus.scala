import java.io.{File, PrintWriter}

import scala.collection.mutable.{ArrayBuffer, HashMap => MutableHashMap}

/**
  * LCSk++ implementation and main method.
  *
  * @author Dario Bosnjak
  */
object LCSkPlusPlus {

  /**
    * Returns true if the pair that starts on (i, j) is a match pair.
    * (i, j) is the start of a match pair if X(i+f) == Y(j+f), for f=[0, k-1]
    *
    * @param i starting index in X
    * @param j starting index in Y
    * @return true if the (i, j) is a match pair, false otherwise
    */
  private def isKMatchPair(X: String, Y: String, i: Int, j: Int, k: Int): Boolean = {
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
  private def getKMatchPairs(X: String, Y: String, k: Int): Array[MatchPair] = {
    val n = X.length
    val m = Y.length
    val freeMem = java.lang.Runtime.getRuntime
    // hash all k-length substrings of X and Y in O(n + m) time
    // key = substring
    // value = an array of starting indices
    val xSubstringStartIndices: MutableHashMap[Long, ArrayBuffer[Int]] = getAllKLengthSubstringStartIndices(X, k)

    val kMatchPairs: ArrayBuffer[MatchPair] = ArrayBuffer[MatchPair]()
    for (j <- 0 until m if j + k <= m) {
      val hash = Y.substring(j, j + k).hashCode
      if (xSubstringStartIndices.get(hash).isDefined) {
        // substring of x is maybe the same as substring of y - because of the hash function
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
  private def getEvents(matchPairs: Array[MatchPair]): Array[Event] = {
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
  private def getEventThatContinues(events: Array[Event], event: Event): Option[Event] = {
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

  /**
    * Runs LCSk++ algorithm for two strings. Returns a similarity between strings.
    *
    * @param X first string
    * @param Y second string
    * @param k parameter k
    * @return similarity between X and Y
    */
  def runLcskPlusPlus(X: String, Y: String, k: Int): Int = {
    val n = X.length
    val m = Y.length

    // due to large input strings we use hash map to represent sparse matrix
    val dp: MutableHashMap[(Int, Int), Int] = MutableHashMap[(Int, Int), Int]()
    val maxColDp = new FenwickTree(m)

    val matchPairs: Array[MatchPair] = getKMatchPairs(X, Y, k)
    val events: Array[Event] = getEvents(matchPairs).sorted
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


  private def runForFile(filePath: String, k: Int, resultsPath: String): Unit = {
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

    val pw = new PrintWriter(new File(resultsPath + "/k=" + k + "-" + fileName))
    pw.write("Similarity: " + similarity.toString)
    pw.flush()
    pw.close()
  }

  private def validatePath(filePath: String): Boolean = {
    val file = new File(filePath)
    if (!(file.isDirectory || file.isFile)) {
      return false
    }
    true
  }

  def main(args: Array[String]): Unit = {
    if (args.length != 3) {
      println("ERROR - 3 arguments required: file path, k, output folder path")
      sys.exit(-1)
    }

    val filePath = args(0)
    val k = args(1).toInt
    val outputDirPath = args(2)

    if (!validatePath(filePath)) {
      println("Input file/directory path is not valid")
      sys.exit(-1)
    }

    // create output directory if it does not exist
    val outputDir = new File(outputDirPath)
    if (!outputDir.exists()) {
      println("Directory " + outputDir.getAbsolutePath + " created")
      outputDir.mkdirs()
    }

    val file = new File(filePath)
    if (file.isDirectory) {
      // run for all files in a directory
      for (currentFile <- file.listFiles()) {
        runForFile(currentFile.getAbsolutePath, k, outputDirPath)
      }
    } else if (file.isFile) {
      // run for a single file
      runForFile(filePath, k, outputDirPath)
    }
  }

}

