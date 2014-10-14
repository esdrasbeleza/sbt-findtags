sbtPlugin := true

name := "sbt-findtags"

version := "0.1-SNAPSHOT"

organization := "com.esdrasbeleza"

description := "sbt plugin to find task tags, like TODO, FIXME etc."

scalacOptions ++= Seq("-unchecked", "-deprecation", "-feature", "-Xfatal-warnings")

libraryDependencies ++= Seq(
  "org.scalatest" %% "scalatest" % "2.2.1" % "test",
  "org.mockito" % "mockito-core" % "1.+" % "test"
)

resolvers ++= Seq("snapshots", "releases").map(Resolver.sonatypeRepo)
