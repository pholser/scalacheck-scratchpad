package fsm

case class Counter(private var n: Int = 0) {
  def increment(): Int = {
    n = n + 1
    n
  }

  def get: Int = n
}
