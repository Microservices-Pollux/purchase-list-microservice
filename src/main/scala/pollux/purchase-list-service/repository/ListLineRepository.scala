package pollux.`purchase-list-service`.repository

import slick.jdbc.PostgresProfile.api._
import scala.concurrent.Future
import pollux.`purchase-list-service`.model.ListLine

 class ListLineTable(tag: Tag) extends Table[ListLine](tag, "list_lines") {
  def id = column[Int]("id", O.PrimaryKey, O.AutoInc) 
  def name = column[String]("name")
  def quantity = column[String]("quantity")
  def addedBy = column[Int]("added_by")
  def dateAdded = column[String]("date_added")
  def purchasedDate = column[Option[String]]("purchased_date")

  def * = (id, name, quantity, addedBy, dateAdded, purchasedDate) <> (ListLine.tupled, ListLine.unapply)
 }


class ListLineRepository(db:Database) {
    val listLines = TableQuery[ListLineTable]
    
    def create(listLine: ListLine): Future[Int] = db.run(listLines += listLine)
    def getAll(): Future[Seq[ListLine]] = db.run(listLines.result)
    def update(id: Int, listLine: ListLine): Future[Int] = db.run(listLines.filter(_.id === id).update(listLine))
    def delete(id: Int): Future[Int] = db.run(listLines.filter(_.id === id).delete)
}