package workshop.model

trait SentimentType {
  def name: String
  override def toString = name
}

object SentimentType {

  def apply(sentimentId: Int) = sentimentId match {
    case 0 => VERY_NEGATIVE
    case 1 => NEGATIVE
    case 2 => NEUTRAL
    case 3 => POSITIVE
    case 4 => VERY_POSITIVE
    case _ => NEUTRAL
  }

}

case object VERY_NEGATIVE extends SentimentType { val name = "very negative"}
case object NEGATIVE extends SentimentType { val name = "negative"}
case object NEUTRAL extends SentimentType { val name = "neutral"}
case object POSITIVE extends SentimentType { val name = "positive"}
case object VERY_POSITIVE extends SentimentType { val name = "very positive"}
