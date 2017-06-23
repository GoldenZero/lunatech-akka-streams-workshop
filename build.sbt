lazy val akkaVersion = "2.5.2"
lazy val twitterVersion = "4.0.6"
lazy val stanfordNLPVersion = "3.8.0"
lazy val circeVersion = "0.8.0"

name := "lunatech-akka-stream-workshop"

version := "1.0"

scalaVersion := "2.12.2"

mainClass in(Compile, run) := Some("com.lunatech.workshops.akka.sentiment.TwitterSentimentAnalyzer")

libraryDependencies ++= Seq(
  //Akka Streams
  "com.typesafe.akka" %% "akka-stream" % akkaVersion,
  "com.typesafe.akka" %% "akka-stream-testkit" % akkaVersion % Test,

  //Twitter
  "org.twitter4j" % "twitter4j-core" % twitterVersion,
  "org.twitter4j" % "twitter4j-stream" % twitterVersion,

  //Standford NLP
  "edu.stanford.nlp" % "stanford-corenlp" % stanfordNLPVersion,
  "edu.stanford.nlp" % "stanford-corenlp" % stanfordNLPVersion classifier "models",

  "com.47deg" %% "github4s" % "0.15.0",

"org.scalatest" %% "scalatest" % "3.0.1" % Test
) ++ Seq(
  "io.circe" %% "circe-core",
  "io.circe" %% "circe-generic",
  "io.circe" %% "circe-parser"
).map(_ % circeVersion)


