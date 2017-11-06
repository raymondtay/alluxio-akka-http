import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.model.StatusCodes._
import akka.http.scaladsl.server._
import akka.stream.ActorMaterializer
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import spray.json._

import scala.util.{Failure, Success}

object Server extends App {
  implicit val system = ActorSystem("alluxio-basic-ops-server")
  implicit val materializer = ActorMaterializer()

  import system.dispatcher

  val route: Route = Routes.millionsongsubsetRoutes
  //val route: Route = Routes.testRoutes

  val h = Http().bindAndHandle(route, "0.0.0.0", sys.props.get("http.port").fold(8080)(_.toInt))
  scala.io.StdIn.readLine()
  h.onComplete(_ => system.terminate)
}
