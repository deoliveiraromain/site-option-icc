import play.Project._

name := "siteicc"

version := "1.0-SNAPSHOT"

libraryDependencies ++= Seq(
  jdbc,
  anorm,
  cache,
  "com.typesafe.play" %% "play-jdbc" % "2.2.1",
  "com.typesafe.slick" %% "slick" % "2.0.0",
  "com.typesafe.play" %% "play-slick" % "0.6.0.1",
  "org.slf4j" % "slf4j-nop" % "1.6.4",
  "com.h2database" % "h2" % "1.3.166",
  "jp.t2v" %% "play2-auth"      % "0.11.0",
  "jp.t2v" %% "play2-auth-test" % "0.11.0" % "test",
  "com.sksamuel.scrimage" % "scrimage-core_2.10" % "1.3.15",
  "com.sksamuel.scrimage" % "scrimage-filters_2.10" % "1.3.15"
)     

play.Project.playScalaSettings
