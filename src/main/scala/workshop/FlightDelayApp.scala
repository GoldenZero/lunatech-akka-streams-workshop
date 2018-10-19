package workshop

import akka.NotUsed
import workshop.model.{FlightDelayRecord, FlightEvent}
import akka.actor.ActorSystem
import akka.stream._
import akka.stream.scaladsl._

import scala.concurrent.Future
import scala.util.Try
import scala.concurrent.ExecutionContext.Implicits._


object FlightDelayApp {

  // implicit actor system
  implicit val system = ActorSystem("flight-delay")
  // implicit actor materializer
  implicit val materializer = ActorMaterializer()


  /**
    * FlowShape
    * Outlet[String]                  FlowShape[String, Option[FlightEvent]]     [FlightEvent, FlightDelayRecord]
    * ╔══════════════════════╗                ╔══════════════════════╗                ╔══════════════════════╗
    * ║                      ║                ║                      ║                ║                      ║
    * ║                      ║                ║                      ║                ║                      ║
    * ║                      ╠──┐          ┌──╣                      ╠──┐          ┌──╣                      ╠──┐
    * ║  Source              │  │─────────▶│  │   csvToFlightEvent & │  │─────────▶│  │   filterAndConvert   │  │──┐
    * ║     .fromIterator()  ╠──┘          └──╣   collectEvents      ╠──┘          └──╣                      ╠──┘  │
    * ║                      ║                ║                      ║                ║                      ║     │
    * ║                      ║                ║                      ║                ║                      ║     │
    * ║                      ║                ║                      ║                ║                      ║     │
    * ╚══════════════════════╝                ╚══════════════════════╝                ╚══════════════════════╝     │
    *                                                                                                              │
    *                                                                                                              │
    *                                                                                      UniformFanOutShape      │
    *                                                 Inlet[Any]       [FlightDelayRecord, FlightDelayRecord]      │
    *                                         ╔══════════════════════╗                ╔══════════════════════╗     │
    *                                         ║                      ║                ║                      ║     │
    *                                         ║                      ║                ║                      ║     │
    *                                         ║                     ┌╩─┐           ┌──╣                      ╠──┐  │
    *                                         ║      Sink.ignore    │  │◀──────────│  │      Partition       │  │◀─┘
    *                                         ║                     └╦─┘           └──╣                      ╠──┘
    *                                         ║                      ║                ║                      ║
    *                                         ║                      ║                ║                      ║
    *                                         ║                      ║                ║                      ║
    *                                         ╚══════════════════════╝                ╚═════════╦──╦═════════╝
    *                                                                                           │  │
    *                                                                                           └──┘
    *                                                        FlowShape                            │
    *       Inlet[Any]         [FlightDelayRecord, (String, Int, Int)]                            │
    * ╔══════════════════════╗                ╔══════════════════════╗                            │
    * ║                      ║                ║                      ║                            │
    * ║                      ║                ║                      ║                            │
    * ║                      ╠──┐          ┌──╣  keepDelayEvents &   ╠──┐                         │
    * ║     Sink.            │  │◀─────────│  │  averageCarrierDelay │  │◀────────────────────────┘
    * ║       foreach(_)     ╠──┘          └──╣                      ╠──┘
    * ║                      ║                ║                      ║
    * ║                      ║                ║                      ║
    * ║                      ║                ║                      ║
    * ╚══════════════════════╝                ╚══════════════════════╝
    */

  def main(args: Array[String]): Unit = {
//    val graph = Streams.delayFlight(Source.fromIterator(() => Streams.flightDelayLines("src/main/resources/2008.csv")))
//    graph.run()
  }
}
