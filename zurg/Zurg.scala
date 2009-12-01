/**
 *  Escape from the Zurg.
 *  http://web.engr.oregonstate.edu/~erwig/papers/Zurg_JFP04.pdf
 */
object Zurg extends Application {
  import Utilities._

  case class Toy(name: String, time: Int) {
    override def toString = name
  }

  sealed abstract class Position
  case object Left  extends Position
  case object Right extends Position

  sealed abstract class Crossing(toys: List[Toy])
  case class CrossLeft(toys: List[Toy])  extends Crossing(toys)
  case class CrossRight(toys: List[Toy]) extends Crossing(toys)
  
  type History = List[Crossing]

  case class State(val left:          List[Toy],
                   val right:         List[Toy],
                   val flashlight:    Position,
                   val timeRemaining: Int) {

    // Search for solution (starting with no history)
    def search: List[History] = search(Nil)
    
    // Depth-first search for solution
    private def search(crossings: History): List[History] = 
      (left, right, flashlight, timeRemaining) match {

        // check if everybody crossed the bridge in time
        case (Nil, _, _, remaining) if (remaining >= 0) =>
            List(crossings)

        // cross to the right
        case (left, right, Left, remaining) if (remaining > 0) => {
          var solutions = List[History]()
          for (pair <- pick2(left)) {
            import pair._
            val left2 = left - _1 - _2
            val right2 =  _1 :: _2 :: right
            val remaining2 = remaining - (_1.time max _2.time)
            val state2 = State(left2, right2, Right, remaining2)
            val crossings2 = CrossRight(List(_1, _2)) :: crossings
            solutions = (state2 search crossings2) ::: solutions
          }
          solutions.reverse
        }

        // cross to the left
        case (left, right, Right, remaining) if (remaining > 0) => {
          var solutions = List[History]()
          for (crossing <- right) {
            val left2 =  crossing :: left
            val right2 = right - crossing
            val remaining2 = remaining - crossing.time
            val state2 = State(left2, right2, Left, remaining2)
            val crossings2 = CrossLeft(List(crossing)) :: crossings
            solutions = (state2 search crossings2) ::: solutions
          }
          solutions.reverse
        }

        // otherwise, no solution
        case _ => List[History]()
    }
  }

  // setup initial state
  val toys = Toy("Buzz", 5) :: Toy("Woody", 10) ::
             Toy("Rex", 20) :: Toy("Hamm", 25)  :: Nil
             
  // kickstart application
  val solutions = State(toys, Nil, Left, 60).search
  
  solutions foreach { Console.println(_) }
}

object Utilities {
  // Return all pair combinations from the list
  def pick2[T](list: List[T]): Seq[Pair[T,T]] = {
    for (i <- 0 until list.length; j <- i+1 until list.length)
      yield (list(i), list(j))
  }
}  
