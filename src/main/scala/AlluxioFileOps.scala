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

object FileOperations {

  def loadLocalFile = Reader{(sourceFilePath: String) =>
    val path = Paths.get(sourceFilePath.toString())
    val data = Files.readAllBytes(path) // reads all the data in-memory

    val buf = ByteBuffer.allocate(data.length)
    buf.order(ByteOrder.nativeOrder())
    buf.put(data)
    buf
  }

  def writeFileOptions = Reader((wt: WriteType) => CreateFileOptions.defaults().setWriteType(wt))

  def readFileOptions = Reader((rt: ReadType) => OpenFileOptions.defaults().setReadType(rt))

  def writeFile(srcData : ByteBuffer, path: String)(implicit fileSystem : FileSystem) = Reader{
    (mWriteOptions:CreateFileOptions) => 
      println("Writing data...")
      val startTimeMs = CommonUtils.getCurrentMs()
      val os = fileSystem.createFile(new AlluxioURI(path), mWriteOptions)
      os.write(srcData.array())
      os.close()

      println(FormatUtils.formatTimeTakenMs(startTimeMs, "writeFile to file " + path))
      true
  }

  def readFile(path: String)(implicit fileSystem : FileSystem) = Reader{
    (mReadOptions:OpenFileOptions) => 
      println("Reading data... from: " + path.toString())
      val startTimeMs = CommonUtils.getCurrentMs()
      val is = fileSystem.openFile(new AlluxioURI(path), mReadOptions)
      val buf = ByteBuffer.allocate(is.remaining().toInt)
      is.read(buf.array())
      buf.order(ByteOrder.nativeOrder())
  
      println("Reading data..." + buf.array().length)
      is.close()

      println(FormatUtils.formatTimeTakenMs(startTimeMs, "readFile file " + path))
      true
  }
}

