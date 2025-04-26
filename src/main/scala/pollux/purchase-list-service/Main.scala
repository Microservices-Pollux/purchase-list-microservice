
package pollux.`purchase-list-service`

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.stream.ActorMaterializer

import slick.jdbc.PostgresProfile.api._

import scala.concurrent.ExecutionContextExecutor
import scala.io.StdIn

import pollux.`purchase-list-service`.repository.ListLineRepository
import pollux.`purchase-list-service`.service.ListLineService
import pollux.`purchase-list-service`.api.ListLineRoutes


object Main {
  def main(args: Array[String]): Unit = {
    implicit val system: ActorSystem = ActorSystem("purchase-list-service")
    implicit val materializer: ActorMaterializer = ActorMaterializer()
    implicit val executionContext: ExecutionContextExecutor = system.dispatcher

    val db = Database.forConfig("db")
    val listLineRepository = new ListLineRepository(db)
    val listLineService = new ListLineService(listLineRepository)
    val listLineRoutes = new ListLineRoutes(listLineService).routes


    val bindingFuture = Http().newServerAt("localhost", 8080).bind(listLineRoutes)
    println("Server online at http://localhost:8080\nPress RETURN to stop...")

    StdIn.readLine()
    bindingFuture
      .flatMap(_.unbind())
      .onComplete(_ => system.terminate())
  }
}