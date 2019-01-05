import scala.collection.mutable.ArrayBuffer
import scala.io.Source

object FastaReader {
  def parseFastaFile(filePath: String): ArrayBuffer[String] = {
    val lineIterator = Source.fromFile(filePath).getLines()
    val sequences: ArrayBuffer[String] = ArrayBuffer[String]()
    var i = 0
    sequences.append("")

    var header = false
    var newSequence = false

    // iterate sequences
    if (lineIterator.hasNext) {
      val firstLine = lineIterator.next()
      // first header
      println("header: ", firstLine)
      // iterate through one sequence
      while (lineIterator.hasNext && !header) {
        val line = lineIterator.next()
        if (line.startsWith(",")) {
          // ignore comments
          println("comment: ", line)
        }
        else if (line.startsWith(">")) {
          println("header: ", line)
          newSequence = true
        } else {
          if (!newSequence)
            sequences(i) = sequences.last ++ line
          else {
            sequences.append("")
            i += 1
            sequences(i) = sequences.last ++ line
            newSequence = false
          }
        }
      }
    }
    sequences
  }


  // DEMO
  def main(args: Array[String]): Unit = {
    val file = "./data/proba.txt"
    println(FastaReader.parseFastaFile(file).toList)
  }
}
