package workshop

import twitter4j.{Status, StatusUpdate, TwitterFactory}
import workshop.model.{Country, Tweet}

/**
  * Created by muayad on 6/30/17.
  */
class TwitterApi {

  val twitter = TwitterFactory.getSingleton

  def post(message: String): Tweet = {
    Tweet(twitter.updateStatus(message))
  }

  def reply(tweet: Tweet, message: String): Tweet = {
    val update = new StatusUpdate(s"${message} @${tweet.author.screenName}")
    update.setInReplyToStatusId(tweet.id)
    Tweet(twitter.updateStatus(update))
  }

}
