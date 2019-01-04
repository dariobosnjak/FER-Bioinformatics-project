class FenwickTree(capacity: Int) {
  private val _tree: Array[(Int, Event)] = Array.fill(capacity + 1) {
    (0, new Event(-1, -1, Event.START))
  }

  def tree: Array[(Int, Event)] = _tree

  private val length = tree.length

  private val lobit = (i: Int) => i & -i

  /**
    * Returns max(array[0, j>)
    *
    * @param j - end index, exclusive
    */
  def max(j: Int): Int = {
    var i = j // for inclusive end index add + 1
    var max: Int = 0
    var parentNode = new Event(-1, -1, Event.START)

    while (i > 0) {
      val (value, node) = tree(i)
      if (value > max) {
        max = value
        parentNode = node
      }
      i -= lobit(i)
    }
    max
  }

  def update(j: Int, newValue: Int, parentNode: Event): Unit = {
    var i = j + 1
    while (i < length) {
      val (value, _) = tree(i)
      if (value <= newValue) {
        tree(i) = (newValue, parentNode)
      }
      i += lobit(i)
    }
  }
}

// DEMO
object FenwickTree {
  def main(args: Array[String]): Unit = {
    val ft = new FenwickTree(10)
    println(ft.tree.toList)
    ft.update(3, 7, ft.tree(3)._2)
    println(ft.tree.toList)
    ft.update(2, 3, ft.tree(2)._2)
    println(ft.tree.toList)
    ft.update(8, 6, ft.tree(8)._2)
    println(ft.tree.toList)
    println(ft.max(2))
    println(ft.max(9))
  }
}
