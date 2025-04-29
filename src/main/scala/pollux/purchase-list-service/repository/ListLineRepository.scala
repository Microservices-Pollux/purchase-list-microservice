package pollux.`purchase-list-service`.repository

import slick.jdbc.PostgresProfile.api._
import scala.concurrent.Future
import pollux.`purchase-list-service`.model.ListLine
import pollux.`purchase-list-service`.model.CreateListLine
import java.time.LocalDate

class ListLineTable(tag: Tag) extends Table[ListLine](tag, "list_lines") {
  def id = column[Int]("id", O.PrimaryKey, O.AutoInc)
  def name = column[String]("name")
  def quantity = column[String]("quantity")
  def addedBy = column[Int]("added_by")
  def dateAdded = column[LocalDate]("date_added")
  def purchasedDate = column[Option[LocalDate]]("purchased_date")

  def * = (
    id,
    name,
    quantity,
    addedBy,
    dateAdded,
    purchasedDate
  ) <> (ListLine.tupled, ListLine.unapply)
}

class ListLineRepository(db: Database) {
  import scala.concurrent.ExecutionContext.Implicits.global

  val listLines = TableQuery[ListLineTable]

  def create(createListLine: CreateListLine): Future[Int] = {
    val insertQuery = listLines
      .map(line =>
        (
          line.name,
          line.quantity,
          line.addedBy,
          line.dateAdded,
          line.purchasedDate
        )
      )
      .returning(listLines.map(_.id))

    val action = insertQuery += ((
      createListLine.name,
      createListLine.quantity,
      createListLine.addedBy,
      createListLine.dateAdded,
      createListLine.purchasedDate
    ))

    db.run(action)
  }

  def getAll(): Future[Seq[ListLine]] = db.run(listLines.result)
  def update(id: Int, listLine: ListLine): Future[Int] =
    db.run(listLines.filter(_.id === id).update(listLine))
  def delete(id: Int): Future[Int] =
    db.run(listLines.filter(_.id === id).delete)
}
