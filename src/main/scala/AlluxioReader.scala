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

import cats._, data._, implicits._

object AlluxioReader {

  // This assumes the file is already found on the FileSystem abstracted by
  // Alluxio
  def readFile(path : String,
               readType : ReadType) : Either[Throwable, Boolean] = {
    import FileOperations._
    for {
      fs       <- Monad[Id].pure(FileSystem.Factory.get())
      rOptions <- readFileOptions(readType)
    } yield {
      Try(FileOperations.readFile(path)(fs).run(rOptions)).toEither.map(_.booleanValue)
    }
  }

}

