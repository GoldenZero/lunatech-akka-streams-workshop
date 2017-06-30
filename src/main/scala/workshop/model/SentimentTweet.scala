package workshop.model


final case class SentimentTweet(
                                 tweet: Tweet,
                                 sentiment: Int
                               ) {
  val sentimentType: SentimentType = SentimentType(sentiment)
  val isPositive = (sentimentType == VERY_POSITIVE || sentimentType == POSITIVE)
  val isNegative = !isPositive
}