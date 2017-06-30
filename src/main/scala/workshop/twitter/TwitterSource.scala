package workshop.twitter

import akka.NotUsed
import akka.actor.ActorSystem
import akka.stream.scaladsl.{Keep, Sink, Source}
import akka.stream.{ActorMaterializer, OverflowStrategy}
import workshop.model._
import twitter4j._
import scala.collection.JavaConverters._

/**
  * Listener that creates an Akka source from a Twitter4j twitter stream
  *
  * Author: Muayad
  */
class TwitterSource(implicit actorSystem: ActorSystem, materializer: ActorMaterializer) {


  private val twitterStream: TwitterStream = TwitterStreamFactory.getSingleton
  private val twitter: Twitter = TwitterFactory.getSingleton

  /**
    * Registers listener to twitterStream and starts listening to all english tweets
    *
    * @return Akka Source of Tweets
    */

  def listen(query: Option[FilterQuery] = None): Source[Tweet, NotUsed] = ???

  def close() = {
    twitterStream.cleanUp()
  }
}