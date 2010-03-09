Example:

case class Input(val x: Int)                            
case class Output(val x: Int)                           

import scala.collection._

val inputs = Array.fill[Input](1000) { Input(1) }

val mr = new MapReduce[Input, Output] {                  
 def map(input: Input) = {
   // double input value
   Output(input.x*2)
 }
 
 def reduce(o1: Output, o2: Output) = {
   // add up all outputs
   Output(o1.x+o2.x)
 }
}        

val future = mr.submit(inputs)
future.get

