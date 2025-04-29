package pollux.`purchase-list-service`.model

import java.time.LocalDate

case class ListLine(id: Int,name: String, quantity: String, addedBy: Int, dateAdded: LocalDate, purchasedDate: Option[LocalDate])

case class CreateListLine(name: String, quantity: String, addedBy: Int, dateAdded: LocalDate, purchasedDate: Option[LocalDate] = None) {
  def toListLine(id: Int): ListLine = ListLine(id, name, quantity, addedBy, dateAdded, purchasedDate)
}