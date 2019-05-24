scalaVersion := "2.12.8"

name := "reactive-message-distribution"
organization := ""
version := "1.0"

mainClass in (Compile, run) := Some("demo.Main")

libraryDependencies += "org.typelevel" %% "cats-core" % "1.6.0"
libraryDependencies += "io.vertx" %% "vertx-lang-scala" % "3.7.0"
libraryDependencies += "io.vertx" % "vertx-mail-client" % "3.7.0"

