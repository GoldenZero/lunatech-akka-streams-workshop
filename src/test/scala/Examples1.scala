import java.nio.file.Paths
import java.util.Date

import akka.{Done, NotUsed}
import akka.actor.ActorSystem
import akka.stream.{ActorMaterializer, IOResult, ThrottleMode}
import workshop.model.{Country, Hashtag, Tweet}
import akka.stream.scaladsl._
import akka.pattern.pipe
import akka.stream.testkit.scaladsl.TestSink
import akka.testkit.TestProbe
import akka.util.ByteString
import org.scalatest.FunSuite

import scala.concurrent.{Await, Future}
import scala.concurrent.duration._

class Examples1 extends FunSuite {
  implicit val system = ActorSystem("workshop-tweets")
  implicit val materializer = ActorMaterializer()

  import system.dispatcher //for futures' execution context


  //Q1: Write a stream that take List(1, 2, 3) as Source and printl these numbers in a Sink
  ignore("My first stream") {
    //source: something with output
    //this source build from 3-elements list
    //(type of outgoing single element is Int, materialization type is NotUsed)
    val source: Source[Int, NotUsed] = Source(List(1, 2, 3))

    //sink: something with input
    //a sink that prints out incoming elements
    //(type of incoming element may be Any, materialization type is Future[Done])
    val sink: Sink[Int, Future[Done]] = Sink.foreach[Int](println)

    //stream blueprint, nothing running yet
    //(materialization type of whole graph is NotUsed)
    val stream: RunnableGraph[NotUsed] = source.to(sink)
    //running the stream
    stream.run
  }

  //Q2: What this code will print
  test("using given stream blueprint twice") {
    val stream = Source((1 to 3)).to(Sink.foreach(println))
    stream.async.run
    stream.async.run
    //TODO try stream.async.run
  }

  //using given stream blueprint twice, but using Sink.foreach materialization to sequence the executions
  ignore("using given stream blueprint twice, but using Sink.foreach materialization to sequence the executions") {
    //materialization type of whole graph is taken from Sink.foreach stage
    val stream: RunnableGraph[Future[Done]] = Source((1 to 3)).toMat(Sink.foreach(println))(Keep.right)
    Await.result(
      for {
        _ <- stream.run
        _ <- stream.run
      } yield (),
      Duration.Inf
    )
  }

  //simple sources
  ignore("simple sources") {
    val stream1 = Source.single(1).to(Sink.foreach(println))
    val stream2 = Source.repeat(1).take(5).to(Sink.foreach(println))
    val iterator = Iterator.from(1)
    val stream3 = Source.fromIterator(() => iterator).take(5).to(Sink.foreach(println))
    val stream4 = Source.tick(0.seconds, 100.millis, "a").to(Sink.foreach(println))
    stream3.run
    //TODO run other streams, one at a time
  }

  //flow
  ignore("flow") {
    //flow: something with input and output
    //type of incoming element is Int, type of outgoing element is Int, materialization type is NotUsed
    //Flow[Int](.apply) is a helper constructor to create a Flow[Int, Int, ...] which outputs all inputs without modification
    val flow: Flow[Int, Int, NotUsed] = Flow[Int].map(_ + 1)
    val source = Source(List(1, 2, 3))
    val sink = Sink.foreach(println)
    val stream = source.via(flow).to(sink)
    stream.run
  }

  //source and sink from flow
  ignore("source and sink from flow") {
    val flowToStr: Flow[Int, String, NotUsed] = Flow[Int].map(_.toString + "0")
    //notice the types: Source[Int, ...] via Flow[Int, String, ...] becomes Source[String, ...]
    val source: Source[String, NotUsed] = Source(List(1, 2, 3)).via(flowToStr)
    //another way to construct flow: fromFunction
    val flowFromString: Flow[String, Int, NotUsed] = Flow.fromFunction(_.toInt)
    //notice the types: Flow[String, Int, ...] to Sink[Any, ...] becomes Sink[String, ...]
    val sink: Sink[String, NotUsed] = flowFromString.to(Sink.foreach(println))
    val stream = source.to(sink)
    stream.run
  }

  //flow from sink and source
  ignore("flow from sink and source") {
    //input of the flow is made from a sink, and output is made from source
    //the sink and source are not connected to each other
    val flow: Flow[Any, Int, NotUsed] = Flow.fromSinkAndSource(Sink.ignore, Source(List(5, 6, 7)))
    val stream = Source.repeat("aaa").via(flow).to(Sink.foreach(println))
    stream.run
  }

  val animalsMap = Map(1 -> "1. tiger", 2 -> "2. lion", 3 -> "3. zebra")

  //dummy "remote" service call, which delays the answer by elem * 200 milliseconds
  def callRemoteService(elem: Int): Future[String] = Future {
    println(s"start call for $elem")
    Thread.sleep(elem * 200)
    println(s"finished call for $elem")
    animalsMap(elem)
  }


  //filter
  ignore("filter"){
    //take natural numbers, add filter pssing even numbers though, take 5 of those and print out
    val naturalNumbersSource = Source.fromIterator(() => Iterator.from(1))
    val stream = naturalNumbersSource.filter(_ % 2 == 0).take(5).to(Sink.foreach(println))
    stream.run
  }

  //reduce
  ignore("reduce"){
    val naturalNumbersSource = Source.fromIterator(() => Iterator.from(1))
    val stream = naturalNumbersSource.take(10).reduce(_ + _).to(Sink.foreach(println))
    stream.run
  }

  //scan: simple example - calculating factorial
  //scan is like fold, but emitting all intermediate states
  ignore("calculating factorial"){
    val naturalNumbersSource = Source.fromIterator(() => Iterator.from(1))
    val stream = naturalNumbersSource.take(10).scan(1)(_ * _).to(Sink.foreach(println))
    stream.run
  }

  //scan: modifying position according to directions
  ignore("modifying position according to directions"){
    val directions = Source("nsseewnn".toList)
    val modifyPosition: ((Int, Int), Char) => (Int, Int) = {
      case ((x, y), 'n') => (x - 1, y)
      case ((x, y), 's') => (x + 1, y)
      case ((x, y), 'e') => (x, y + 1)
      case ((x, y), 'w') => (x, y - 1)
    }
    val stream = directions
      .scan((0, 0))(modifyPosition)
      .to(Sink.foreach(println))
    stream.run
  }

  //mapAsync
  ignore("mapAsync"){
    //processing serveral elements through remote service call
    //mapAsync(1) means parallelism = 1
    //you can observe that there is one call at a time
    //toMat(Sink.foreach(println))(Keep.right) is needed to obtain Future[Done] during materialization
    //so we can await stream completion
    val stream: RunnableGraph[Future[Done]] = Source(List(3, 2, 1, 3, 2, 1)).mapAsync(1)(callRemoteService).toMat(Sink.foreach(println))(Keep.right)
    Await.result(stream.run, Duration.Inf)
  }

  //mapAsync with higher level of parallelism
  ignore("mapAsync with higher level of parallelism"){
    //now, with mapAsync(2) you can observe that remote service calls are processed in parallel
    //the results are still printed in order by which they came out of the source
    val stream: RunnableGraph[Future[Done]] = Source(List(3, 2, 1, 3, 2, 1)).mapAsync(2)(callRemoteService).toMat(Sink.foreach(println))(Keep.right)
    Await.result(stream.run, Duration.Inf)
  }

  //mapAsyncUnordered(2)
  ignore("mapAsyncUnordered(2)"){
    //mapAsyncUnordered doesn't have to keep order on output
    val stream: RunnableGraph[Future[Done]] = Source(List(3, 2, 1, 3, 2, 1)).mapAsyncUnordered(2)(callRemoteService).toMat(Sink.foreach(println))(Keep.right)
    Await.result(stream.run, Duration.Inf)
  }

  //mapAsyncUnordered(3)
  ignore("mapAsyncUnordered(3)"){
    //even higher level of parallelism
    val stream: RunnableGraph[Future[Done]] = Source(List(3, 2, 1, 3, 2, 1)).mapAsyncUnordered(3)(callRemoteService).toMat(Sink.foreach(println))(Keep.right)
    Await.result(stream.run, Duration.Inf)
  }

  //throttle - passing 1 element per specificic time interval
  ignore("throttle - passing 1 element per specificic time interval"){
    //below example behaves basically like Source.tick with interval of 1 second:
    val stream = Source(List(1, 2, 3)).throttle(1, 1.seconds, 1, ThrottleMode.shaping).toMat(Sink.foreach(println))(Keep.right)
    Await.result(stream.run, Duration.Inf)
  }

  //exercise: use throttle to make stream ordered even with mapAsyncUnordered (in code from example8)
  //run with sbt "run 2.10"
  ignore("use throttle to make stream ordered even with mapAsyncUnordered (in code from example8)"){
    //modify below code, add throttle:
    val stream: RunnableGraph[Future[Done]] = Source(List(3, 2, 1, 3, 2, 1)).mapAsyncUnordered(3)(callRemoteService).toMat(Sink.foreach(println))(Keep.right)
    Await.result(stream.run, Duration.Inf)
  }
}
