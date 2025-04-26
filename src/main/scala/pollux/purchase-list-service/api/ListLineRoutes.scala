package pollux.`purchase-list-service`.api

import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Route
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import spray.json._
import pollux.`purchase-list-service`.model.ListLine
import pollux.`purchase-list-service`.service.ListLineService



class ListLineRoutes(listLineService: ListLineService) extends DefaultJsonProtocol {
  implicit val listLineFormat = jsonFormat6(ListLine)

  val routes: Route =
    pathPrefix("listlines") {
      concat(
        pathEndOrSingleSlash {
          concat(
            post {
              entity(as[ListLine]) { listLine =>
                onSuccess(listLineService.create(listLine)) { _ =>
                  complete(StatusCodes.Created)
                }
              }
            },
            get {
              onSuccess(listLineService.getAll()) { listLines =>
                complete(listLines)
              }
            }
          )
        }
      )
    }
}
