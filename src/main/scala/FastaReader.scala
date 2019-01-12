import scala.collection.mutable.ArrayBuffer
import scala.io.Source

/**
  * Represents one sequence in FASTA file.
  *
  * @param header sequence ID/name
  * @param sequence sequence of characters
  * @param comments comments
  *
  * @author Dario Bosnjak
  */
case class Sequence(header: String, sequence: String, comments: ArrayBuffer[String])

/**
  * Reads FASTA files and returns a collection of Sequence instances.
  * FASTA file format:
  *   > sequence ID
  *   sequence in one or more lines
  *   , comment
  *
  * Dario Bosnjak.
  */
object FastaReader {
  def parseFastaFile(filePath: String): Seq[Sequence] = {
    val lineIterator = Source.fromFile(filePath).getLines()

    val headers: ArrayBuffer[String] = ArrayBuffer[String]()
    val sequences: ArrayBuffer[String] = ArrayBuffer[String]()
    val comments: ArrayBuffer[ArrayBuffer[String]] = ArrayBuffer[ArrayBuffer[String]]()

    // initialize
    var seqId = 0
    sequences.append("")
    comments.append(ArrayBuffer[String]())

    var header = false
    var newSequence = false

    var result = ArrayBuffer[Sequence]()

    // iterate sequences
    if (lineIterator.hasNext) {
      val firstLine = lineIterator.next()
      // first header
      headers.append(firstLine)
      // iterate through one sequence
      while (lineIterator.hasNext && !header) {
        val line = lineIterator.next()
        if (line.startsWith(",")) {
          // ignore comments
          comments.last.append(line)
        }
        else if (line.startsWith(">")) {
          // new header
          newSequence = true
          // end previous sequence
          result.append(Sequence(headers.last, sequences.last, comments.last))
          // start a new one
          headers.append(line)
          comments.append(ArrayBuffer[String]())
        } else {
          if (!newSequence) {
            // continuation of a sequence
            sequences(seqId) = sequences.last ++ line
          }
          else {
            // start new sequence
            sequences.append("")
            seqId += 1
            sequences(seqId) = sequences.last ++ line
            newSequence = false
          }
        }
      }
      // append last one
      result.append(Sequence(headers.last, sequences.last, comments.last))
    }

    result
  }


  // DEMO
  def main(args: Array[String]): Unit = {
    val file = "data/proba.txt"
    FastaReader.parseFastaFile(file)
  }
}
