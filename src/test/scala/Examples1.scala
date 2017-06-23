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
    val sink: Sink[Any, Future[Done]] = Sink.foreach(println)

    //stream blueprint, nothing running yet
    //(materialization type of whole graph is NotUsed)
    val stream: RunnableGraph[NotUsed] = source.to(sink)
    //running the stream
    stream.run
  }

}
