package pollux.`purchase-list-service`.service

import pollux.`purchase-list-service`.model.ListLine
import pollux.`purchase-list-service`.repository.ListLineRepository
import scala.concurrent.Future

class ListLineService(listLineRepository: ListLineRepository) {
  def create(listLine: ListLine): Future[Int] = listLineRepository.create(listLine)
  def getAll(): Future[Seq[ListLine]] = listLineRepository.getAll()
}