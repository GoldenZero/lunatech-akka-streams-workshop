package workshop

import java.util.Properties

import edu.stanford.nlp.ling.CoreAnnotations
import edu.stanford.nlp.neural.rnn.RNNCoreAnnotations
import edu.stanford.nlp.pipeline.StanfordCoreNLP
import edu.stanford.nlp.sentiment.SentimentCoreAnnotations

import scala.collection.JavaConverters._


trait SentimentAnalyzer {
  def calculateSentiment(message: String): Int
}

class SentimentAPI extends SentimentAnalyzer {

  val props = new Properties();
  props.setProperty("annotators", "tokenize, ssplit, pos, lemma, parse, sentiment")
  val pipeline = new StanfordCoreNLP(props)


  def calculateSentiment(message: String): Int = Option(message) match {
    case Some(text) if !text.isEmpty => extractSentiment(text)
    case _ => throw new IllegalArgumentException("input can't be null or empty")
  }

  private def extractSentiment(message: String): Int = {

    val annotation = pipeline.process(message)
    val sentences = annotation.get(classOf[CoreAnnotations.SentencesAnnotation])

    val sentiments: Seq[Int] = sentences.asScala.map(sentence => sentence.get(classOf[SentimentCoreAnnotations.SentimentAnnotatedTree]))
      .map(tree => RNNCoreAnnotations.getPredictedClass(tree))

    math.ceil(sentiments.foldLeft(0.0)(_ + _) / sentiments.length).toInt
  }
}
