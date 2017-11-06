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
import io.opentracing.util.GlobalTracer

object Routes {

  Tracer.globalTracer // use the default tracer

  // simple stuff to illustrate opentracing.io via netflix's jaegar
  val helloWorldRoute = (get & path("hello")) {
    implicit val span = GlobalTracer.get().buildSpan("hello_world_trace").startActive()
    Tracer.closeAfterLog("Test", "Hello World!")({})
    complete(OK)
  }

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
              implicit val span = GlobalTracer.get().buildSpan("read_from_alluxio").startActive()
              writeFile(src, dest, wt) match {
                case Left(_) =>
                  span.close()
                  complete(custom(NotAcceptable.intValue, "Could not find the file you specified."))
                case Right(_) =>
                  span.close()
                  complete(OK)
              }
          }
       }
      } ~
      (get & path("load")) {
        parameters('sPath, 'readtype) { (src, rt) =>
          import AlluxioReader.readFile
          import scala.util.Try

          val rtype : Option[alluxio.client.ReadType] = Try(alluxio.client.ReadType.valueOf(rt)).toOption
          implicit val span = GlobalTracer.get().buildSpan("read_from_alluxio").startActive()
          readFile(src, rtype.get) match {
            case Left(_) =>
              span.close()
              complete(custom(NotAcceptable.intValue, "Could not find the file you specified."))
            case Right(_) =>
              span.close()
              complete(OK)
          }
        }
      }

    }
}
