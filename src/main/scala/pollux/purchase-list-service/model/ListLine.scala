package pollux.`purchase-list-service`.model

case class ListLine(id: Int,name: String, quantity: String, addedBy: Int, dateAdded: String, purchasedDate: Option[String])
