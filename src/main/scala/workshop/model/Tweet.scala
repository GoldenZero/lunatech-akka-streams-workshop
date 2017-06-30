package workshop.model

import java.util.Date

import twitter4j.Status


final case class Tweet(
                        id: Long,
                        message: String,
                        author: Author,
                        created_at: Date,
                        lang: Option[String],
                        country: Option[Country],
                        retweets: Int
                      ) {
  def hashTags: Set[Hashtag] = message.split(" |\n").collect { case t if t.startsWith("#") => Hashtag(t) }.toSet
}

object Tweet {
  def apply(status: Status): Tweet = {
    new Tweet(
      id = status.getId,
      message = status.getText,
      author = Author(status.getUser.getName,status.getUser.getScreenName),
      created_at = status.getCreatedAt,
      lang = Option(status.getLang),
      country = Option(status.getPlace).map(p => Country(p.getCountryCode)),
      retweets = status.getRetweetCount
    )
  }
}

final case class Country(country_code: String)

final case class Entities(hashtags: Seq[Hashtag], urls: Seq[Url], media: Option[Seq[Media]])

final case class Media(expanded_url: String)

final case class Hashtag(text: String)

final case class Url(expanded_url: String)

final case class Author(name: String, screenName: String)