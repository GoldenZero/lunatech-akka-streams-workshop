lazy val akkaVersion = "2.5.17"
lazy val circeVersion = "0.10.0"

name := "lunatech-akka-stream-workshop"

version := "1.0"

scalaVersion := "2.12.7"

mainClass in(Compile, run) := Some("workshop.FlightDelayApp")

libraryDependencies ++= Seq(

  //Akka Streams
  "com.typesafe.akka" %% "akka-stream" % akkaVersion,
  "com.typesafe.akka" %% "akka-stream-testkit" % akkaVersion % Test,

  "com.lightbend.akka" %% "akka-stream-alpakka-file" % "0.20",
  "com.lightbend.akka" %% "akka-stream-alpakka-csv" % "0.20",

  // akka-http dependencies
  "com.typesafe.akka" %% "akka-http" % "10.1.5",
  "com.typesafe.akka" %% "akka-http-testkit" % "10.1.5" % Test,

  "com.47deg" %% "github4s" % "0.15.0",

  "com.typesafe" % "config" % "1.3.1",
  "ch.qos.logback" % "logback-classic" % "1.1.2",
  "com.typesafe.scala-logging" %% "scala-logging" % "3.9.0",

  "org.scalacheck" %% "scalacheck" % "1.14.0" % Test,
  "org.scalatest" %% "scalatest" % "3.0.1" % Test
) ++ Seq(
  "io.circe" %% "circe-core",
  "io.circe" %% "circe-generic",
  "io.circe" %% "circe-parser"
).map(_ % circeVersion)


