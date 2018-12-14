package hmda.census

import akka.actor.ActorSystem
import akka.grpc.GrpcClientSettings
import akka.stream.ActorMaterializer
import hmda.grpc.services._

import scala.concurrent.Await
import scala.concurrent.duration._

object CensusApiTest extends App {
  implicit val clientSystem = ActorSystem("CheckDigitClient")
  implicit val materializer = ActorMaterializer()
  implicit val ec = clientSystem.dispatcher

  val client = CensusServiceClient(
    GrpcClientSettings.connectToServiceAt("0.0.0.0", 60080).withTls(false))

  val replyF =
    client.validateTract(ValidTractRequest("78030961200"))

  val result = Await.result(replyF, 2.seconds)
  println("Tract IS VALID?: " + result)

  val replyC =
    client.validateCounty(ValidCountyRequest("78030"))

  val resultCounty = Await.result(replyC, 2.seconds)
  println("County IS VALID?: " + resultCounty)

  val reply30k =
    client.validatePopulation(ValidPopulationRequest("18030"))

  val result30k = Await.result(reply30k, 2.seconds)
  println("County30k IS VALID?: " + result30k)

}
