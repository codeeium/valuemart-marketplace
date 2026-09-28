name := "scala-hello-world"

version := "0.1.0"

scalaVersion := "3.3.8"

libraryDependencies += "org.scalatest" %% "scalatest" % "3.2.17" % Test

fork := true

Compile / run / mainClass := Some("Server")
