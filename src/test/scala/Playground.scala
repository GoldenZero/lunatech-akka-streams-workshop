//import java.util.Date
//
//import akka.NotUsed
//import akka.actor.ActorSystem
//import akka.stream.ActorMaterializer
//import workshop.model.{Country, Hashtag, Tweet}
//import akka.stream.scaladsl._
//import akka.pattern.pipe
//
//import scala.concurrent.Await
//import scala.concurrent.duration._
//
//class Playground extends org.scalatest.FunSuite {
//  implicit val system = ActorSystem("workshop-tweets")
//  implicit val materializer = ActorMaterializer()
//  import system.dispatcher
//
//
//
////  test("a sink") {
////
////    val sinkUnderTest = Flow[Int].map(_ * 2).toMat(Sink.fold(0)(_ + _))(Keep.right)
////
////    val future = Source(1 to 4).runWith(sinkUnderTest)
////    val result = Await.result(future, 3.seconds)
////    assert(result == 20)
////  }
////
////  test("a Source"){
////    val sourceUnderTest = Source.repeat(1).map(_ * 2)
////
////    val future = sourceUnderTest.take(10).runWith(Sink.seq)
////    val result = Await.result(future, 3.seconds)
////    assert(result == Seq.fill(10)(2))
////  }
////
////  test("a flow"){
////
////  }
////
////
////  val tweets: Source[Tweet, NotUsed] = Source(
////    Tweet("#akka #lunatech", "author1", new Date(), Some("en"), Some(Country("NL")), 0) ::
////      Tweet("my #akka", "author1", new Date(), Some("en"), Some(Country("NL")), 0) ::
////      Tweet("attending #akka_streams", "author2", new Date(), Some("en"), Some(Country("NL")), 0) ::
////      Tweet("#apples rock!", "author4", new Date(), Some("en"), Some(Country("NL")), 0) ::
////      Nil)
////
////  ignore("akka tags") {
////    val akkaTag = Hashtag("#akka")
////    tweets
////      //.filterNot(_.hashTags.contains(akkaTag))
////      .filter(_.hashTags.contains(akkaTag))
////      .mapConcat(_.hashTags)
////      .map(_.text.toUpperCase)
////      .runWith(Sink.foreach(println))
////
////  }
//
//}
