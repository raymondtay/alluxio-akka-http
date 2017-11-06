
import com.uber.jaeger.Configuration
import io.opentracing.ActiveSpan
import io.opentracing.util.GlobalTracer

object Tracer {

  // default tracer for this example application
  lazy val globalTracer =
    GlobalTracer.register(
    new Configuration(
        "AlluxioReaderWriter",
        new Configuration.SamplerConfiguration("const", 1),
        new Configuration.ReporterConfiguration(
            false, "localhost", null, 1000, 10000)
    ).getTracer())

  // small utility to close the span after we are done.
  def closeAfterLog[E,A](prefix: String, msg: String)(f: => A)(implicit span: ActiveSpan) =
    scala.util.Try{
      span.log(s"[$prefix] $msg")
      val result = f
      span.close()
      result
    }.toEither
}
