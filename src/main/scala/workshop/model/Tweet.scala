package workshop.model

import java.util.Date


final case class Tweet(
                        message: String,
                        author: String,
                        created_at: Date,
                        //                        entities: Entities,
                        //                        favorite_count: Option[Int],
                        //                        filter_level: String,
                        //                        id_str: String,
                        lang: Option[String],
                        country: Option[Country],
                        retweets: Int
                      ) {
  def hashTags: Set[Hashtag] = message.split(" |\n").collect { case t if t.startsWith("#") => Hashtag(t) }.toSet
}

final case class Country(country_code: String)

final case class Entities(hashtags: Seq[Hashtag], urls: Seq[Url], media: Option[Seq[Media]])

final case class Media(expanded_url: String)

final case class Hashtag(text: String)

final case class Url(expanded_url: String)
