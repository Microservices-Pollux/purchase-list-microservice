package pollux.`purchase-list-service`.api

import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Route
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import pollux.`purchase-list-service`.model.{ListLine, CreateListLine}
import pollux.`purchase-list-service`.model.JsonFormats._ 
import pollux.`purchase-list-service`.service.ListLineService

class ListLineRoutes(listLineService: ListLineService) {
  val routes: Route =
    pathPrefix("listlines") {
      concat(
        pathEndOrSingleSlash {
          concat(
            post {
              entity(as[CreateListLine]) { createListLine =>
                onSuccess(listLineService.create(createListLine)) { _ =>
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
