name := "scala-hello-world"
version := "1.0.0"
scalaVersion := "3.3.8"
Compile / mainClass := Some("Server")
Compile / run / fork := true
libraryDependencies += "org.scalatest" %% "scalatest-funsuite" % "3.2.17" % Test
