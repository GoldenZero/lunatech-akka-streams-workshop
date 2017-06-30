package workshop

import java.util.Date
import java.util.concurrent.{ExecutorService, Executors}

import akka.{Done, NotUsed}
import akka.actor.ActorSystem
import akka.stream.scaladsl._
import akka.stream.{ActorMaterializer, ClosedShape}
import workshop.model._
import workshop.twitter.TwitterSource

import scala.concurrent.{ExecutionContext, Future}

object TwitterSentimentAnalyzer extends App {


  val execService: ExecutorService = Executors.newCachedThreadPool()
  implicit val system: ActorSystem = ActorSystem("sentiment")
  implicit val ec: ExecutionContext = ExecutionContext.fromExecutorService(execService)
  implicit val materializer = ActorMaterializer()(system)
  val emptyTweet: Tweet = Tweet(
    id = 0,
    message = "",
    author = Author("", ""),
    created_at = new Date(),
    lang = None,
    country = None,
    retweets = 0
  )

  val out: Sink[SentimentTweet, Future[Done]] = ???


  def log[T]: Flow[T, T, NotUsed] = ???

  // Tweet => Tweet
  def filterLang(langs: Set[String]): Flow[Tweet, Tweet, NotUsed] = ???

  //Tweet => SentimentTweet
  def calculateSentiment(sentimentAnalyser: SentimentAnalyzer): Flow[Tweet, SentimentTweet, NotUsed] = ???
  /**
    * Tweet  ~> Negative
    * ~> Positive
    */
  val sentimentPartitioner: Partition[SentimentTweet] = ???

  def replyTweet(twitterApi: TwitterApi)(message: String): Flow[SentimentTweet, SentimentTweet, NotUsed] = ???



  val tweets: Source[Tweet, NotUsed] = new TwitterSource().listen(None)

  val graphSentiment = RunnableGraph.fromGraph(
    GraphDSL.create() {
      implicit builder: GraphDSL.Builder[NotUsed] =>
        import GraphDSL.Implicits._

        val englishOrFrenchOnly = builder.add(filterLang(Set("en", "fr")))
        val logTweet = builder.add(log[Tweet])
        val nagetiveReply = builder.add(replyTweet(new TwitterApi)(""))
        val positiveReply = builder.add(replyTweet(new TwitterApi)(""))
        val addSentiment = builder.add(calculateSentiment(new SentimentAPI()))
        val partition = builder.add(sentimentPartitioner)

        tweets ~> englishOrFrenchOnly ~> addSentiment ~> partition ~> positiveReply ~> out
                                                         partition ~> out
        //        tweets ~> logTweet ~> englishOrFrenchOnly ~> reply ~> out

        ClosedShape
    }
  )

  graphSentiment.run

}
