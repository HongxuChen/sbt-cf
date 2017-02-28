sbtPlugin := true

name := "sbt-cf"

organization := "com.github.hongxuchen"

version := "0.0.1-SNAPSHOT"

//scalaVersion := "2.11.8"

scalacOptions ++= Seq(
  "-language:_",
  "-deprecation",
  "-encoding", "UTF-8", // yes, this is 2 args
  "-feature",
  "-unchecked",
  //  "-Xfatal-warnings",
  "-Xlint",
  "-Yno-adapted-args",
  "-Ywarn-dead-code"
)

val checkerFrameworkLibs = Seq(
  "org.checkerframework" % "checker" % "2.1.8",
  "org.checkerframework" % "compiler" % "2.1.8",
  "org.checkerframework" % "jdk8" % "2.1.8"
)

libraryDependencies ++= Seq(
  "org.scalatest" %% "scalatest" % "3.0.0" % "test"
)

libraryDependencies ++= checkerFrameworkLibs