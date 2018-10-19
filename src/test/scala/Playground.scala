import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import akka.{Done, NotUsed}
import akka.actor.ActorSystem
import akka.util.ByteString

import scala.concurrent._
import scala.concurrent.duration._
import java.nio.file.Paths

import akka.stream._
import akka.stream.scaladsl._
import org.scalatest.FreeSpec

import scala.concurrent.Await
import scala.concurrent.duration._

class Playground extends FreeSpec {
  implicit val system = ActorSystem("workshop")
  implicit val materializer = ActorMaterializer()


  "a source" in {
    val source: Source[Int, NotUsed] = Source(1 to 100)
    source.runForeach(i ⇒ println(i))(materializer)

  }

  "a sink" in {

    val sinkUnderTest = Flow[Int].map(_ * 2).toMat(Sink.fold(0)(_ + _))(Keep.right)

    val future = Source(1 to 4).runWith(sinkUnderTest)
    val result = Await.result(future, 3.seconds)
    assert(result == 20)
  }

  "Time-Based Processing" in {
    val sources: Source[Int, Promise[Option[Int]]] = Source.maybe[Int]


    val source: Source[Int, NotUsed] = Source(1 to 100)
    val factorials = source.scan(BigInt(1))((acc, next) ⇒ acc * next)
    factorials
      .zipWith(Source(0 to 100))((num, idx) ⇒ s"$idx! = $num")
      .throttle(1, 1.second)
      .runForeach(println)



  }

}
