import bintray.Keys._

sbtPlugin := true

name := "sbt-findtags"

version := "0.4"

organization := "com.esdrasbeleza"

description := "sbt plugin to find task tags, like TODO, FIXME etc."

scalacOptions ++= Seq("-unchecked", "-deprecation", "-feature", "-Xfatal-warnings")

libraryDependencies ++= Seq(
)

resolvers ++= Seq("snapshots", "releases").map(Resolver.sonatypeRepo)

licenses += ("MIT", url("http://opensource.org/licenses/MIT"))

publishMavenStyle := true

publishArtifact in Test := false

pomExtra := (
  <url>https://github.com/esdrasbeleza/sbt-findtags</url>
  <scm>
    <url>git@github.com:esdrasbeleza/sbt-findtags.git</url>
    <connection>scm:git:git@github.com:esdrasbeleza/sbt-findtags.git</connection>
  </scm>
  <developers>
    <developer>
      <id>esdrasbeleza</id>
      <name>Esdras Beleza</name>
      <url>http://esdrasbeleza.com</url>
    </developer>
  </developers>
)

bintrayPublishSettings

repository in bintray := "sbt-plugins"

bintrayOrganization in bintray := None
