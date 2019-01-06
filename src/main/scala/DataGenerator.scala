import java.io._

import scala.util.Random

/**
  * Generates two random sequences of characters: A, C, G, T.
  * Only argument is a file path where sequences are stored.
  * Dario Bosnjak
  */
object DataGenerator {
  val MAX_LINE_LENGTH = 80

  def main(args: Array[String]): Unit = {
    val file = args(0)
    val length: Int = args(1).toInt

    val alphabet = Array("A", "C", "G", "T")
    val random = new Random(System.currentTimeMillis())
    val header = ">sequence-"

    val pw = new PrintWriter(new File(file))
    for (seqId <- Seq(0, 1)) {
      pw.write(header + seqId)
      pw.write(System.lineSeparator())
      for (i <- 0 until length) {
        if (i != 0 && i % MAX_LINE_LENGTH == 0) {
          // file looks better, BUT reading data is SLOW for big files
          // pw.write(System.lineSeparator())
        }
        pw.write(alphabet(random.nextInt(alphabet.length)))
      }
      pw.write(System.lineSeparator())
    }

    pw.flush()
    pw.close()
  }
}
