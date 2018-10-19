package workshop.model

final case class FlightDelayRecord(
                                    year: String,
                                    month: String,
                                    dayOfMonth: String,
                                    flightNum: String,
                                    uniqueCarrier: String,
                                    arrDelayMins: Int) {
  override def toString = s"${year}/${month}/${dayOfMonth} - ${uniqueCarrier} ${flightNum} - ${arrDelayMins}"
}