import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import org.scalatest.FreeSpec

class CustomStageSpec extends FreeSpec {

  "A custom Stage should" - {
    implicit val system = ActorSystem("CustomStageSpec")
    implicit val materializer = ActorMaterializer()

    "be fun" in {
      println("Having fun at ScalaWave!")
    }
  }

}
