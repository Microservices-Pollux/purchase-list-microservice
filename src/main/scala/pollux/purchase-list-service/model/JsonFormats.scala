package pollux.`purchase-list-service`.model

import spray.json._
import DefaultJsonProtocol._
import java.time.LocalDate

object JsonFormats extends DefaultJsonProtocol {
  // Custom format for LocalDate
  implicit object LocalDateFormat extends JsonFormat[LocalDate] {
    def write(date: LocalDate): JsValue = JsString(date.toString)
    def read(json: JsValue): LocalDate = json match {
      case JsString(str) => LocalDate.parse(str)
      case _ => deserializationError("LocalDate expected")
    }
  }
  
  implicit def optionLocalDateFormat: JsonFormat[Option[LocalDate]] = 
    new JsonFormat[Option[LocalDate]] {
      def write(option: Option[LocalDate]): JsValue = option match {
        case Some(date) => LocalDateFormat.write(date)
        case None => JsNull
      }
      def read(json: JsValue): Option[LocalDate] = json match {
        case JsNull => None
        case other => Some(LocalDateFormat.read(other))
      }
    }
  
  implicit val listLineFormat: RootJsonFormat[ListLine] = jsonFormat6(ListLine)
  
  implicit val createListLineFormat: RootJsonFormat[CreateListLine] = new RootJsonFormat[CreateListLine] {
    def write(c: CreateListLine): JsObject = {
      JsObject(
        "name" -> JsString(c.name),
        "quantity" -> JsString(c.quantity),
        "addedBy" -> JsNumber(c.addedBy),
        "dateAdded" -> LocalDateFormat.write(c.dateAdded),
        "purchasedDate" -> optionLocalDateFormat.write(c.purchasedDate)
      )
    }
    
    def read(json: JsValue): CreateListLine = {
      val fields = json.asJsObject.fields
      
      val name = fields("name").convertTo[String]
      val quantity = fields("quantity").convertTo[String]
      val addedBy = fields("addedBy").convertTo[Int]
      val dateAdded = fields("dateAdded").convertTo[LocalDate]
      val purchasedDate = fields.get("purchasedDate") match {
        case Some(JsNull) => None
        case Some(jsVal) => Some(jsVal.convertTo[LocalDate])
        case None => None // This allows purchasedDate to be missing
      }
      
      CreateListLine(name, quantity, addedBy, dateAdded, purchasedDate)
    }
  }
}