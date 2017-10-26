import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.model.StatusCodes._
import akka.http.scaladsl.server._
import akka.http.scaladsl.common.EntityStreamingSupport
import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import akka.stream.scaladsl._
import akka.util.ByteString
import akka.http.scaladsl.unmarshalling._
import akka.http.scaladsl.model.{HttpRequest, HttpEntity}

import akka.stream.alpakka.csv.scaladsl.CsvParsing

object Routes {

  val millionsongsubsetRoutes = 
    pathPrefix("alluxio") {
      (post & path("store")) {
        parameters('sPath, 'dPath, 'writetype) { (src, dest, wt) =>
          import AlluxioWriter.writeFile
          import scala.util.Try

          val wtype : Option[alluxio.client.WriteType] = Try(alluxio.client.WriteType.valueOf(wt)).toOption
          wtype match {
            case None => complete(Forbidden) // invalid write-type option to Alluxio
            case Some(wt) => 
              writeFile(src, dest, wt) match {
                case Left(_) => complete(custom(NotAcceptable.intValue, "Could not find the file you specified."))
                case Right(_) => complete(OK)
              }
          }
       }
      } ~
      (get & path("load")) {
        parameters('sPath, 'readtype) { (src, rt) =>
          import AlluxioReader.readFile
          import scala.util.Try

          val rtype : Option[alluxio.client.ReadType] = Try(alluxio.client.ReadType.valueOf(rt)).toOption
          readFile(src, rtype.get) match {
            case Left(_) => complete(custom(NotAcceptable.intValue, "Could not find the file you specified."))
            case Right(_) => complete(OK)
          }
        }
      }

    }
}
