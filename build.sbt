name := "alluxio-akka-http"
version := "0.1.0-SNAPSHOT"

description := "An example RESTful server written with akka-http and Alluxio."

scalaVersion := "2.12.3"
scalacOptions ++= Seq("-deprecation", "-feature")

libraryDependencies ++= Seq(
  "org.typelevel" %% "cats-core" % "1.0.0-MF",
  "org.alluxio" % "alluxio-core-client-fs" % "1.6.0",
  "com.typesafe.akka" %% "akka-http" % "10.0.9",
  "com.typesafe.akka" %% "akka-http-spray-json" % "10.0.9",
  "com.lightbend.akka" %% "akka-stream-alpakka-cassandra" % "0.13",
  "com.lightbend.akka" %% "akka-stream-alpakka-csv" % "0.13",
  "org.scalatest" %% "scalatest" % "3.0.4" % Test
)

Revolver.settings
enablePlugins(JavaAppPackaging)
