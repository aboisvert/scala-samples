Interactive example:

    Welcome to Scala version 2.8.0.Beta1-prerelease (Java HotSpot(TM) Server VM, Java 1.6.0_17).
    Type in expressions to have them evaluated.
    Type :help for more information.

    scala> :load MapReduce.scala
    Loading MapReduce.scala...
    defined class MapReduce

    scala> case class Input(val x: Int)
    defined class Input

    scala> case class Output(val x: Int)
    defined class Output

    scala> val inputs = Array.fill[Input](1000) { Input(1) }
    inputs: Array[Input] = Array(Input(1), Input(1), Input(1), Input(1), Input(1), ...

    scala> val mr = new MapReduce[Input, Output] {
         |  def map(input: Input) = {
         |    // double input value
         |    Output(input.x*2)
         |  }
         |
         |  def reduce(o1: Output, o2: Output) = {
         |    // add up all outputs
         |    Output(o1.x+o2.x)
         |  }
         | }
    mr: MapReduce[Input,Output] = $anon$1@1c98c1b

    scala> val future = mr.submit(inputs)
    future: java.util.concurrent.Future[Output] = MapReduce$Job@1b31c23

    scala> future.get
    res0: Output = Output(2000)

