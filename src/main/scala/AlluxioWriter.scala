import alluxio.AlluxioURI;
import alluxio.client.ReadType;
import alluxio.client.WriteType;
import alluxio.client.file.FileInStream;
import alluxio.client.file.FileOutStream;
import alluxio.client.file.FileSystem;
import alluxio.client.file.options.CreateFileOptions;
import alluxio.client.file.options.OpenFileOptions;
import alluxio.exception.AlluxioException;
import alluxio.util.CommonUtils;
import alluxio.util.FormatUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.concurrent.Callable;
import scala.language.higherKinds

import scala.util.Try
import scala.collection.JavaConversions._

import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.Path;

import cats._, data._, implicits._

object AlluxioWriter {

  // This API writes the file to the given location
  // and reads it back, giving us the time to read/write.
  def writeFile(srcPath : String,
                destPath : String,
                writeType : WriteType) : Either[Throwable, Boolean] = {
    import FileOperations._
    for {
      fs       <- Monad[Id].pure(FileSystem.Factory.get())
      data     <- loadLocalFile(srcPath)
      wOptions <- writeFileOptions(writeType)
    } yield {
      Try(FileOperations.writeFile(data, destPath)(fs).run(wOptions)).toEither.map(_.booleanValue)
    }
  }

}
