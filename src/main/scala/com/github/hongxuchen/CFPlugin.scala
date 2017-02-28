package com.github.hongxuchen

import sbt.Keys._
import sbt._

class JDKVersionUnsupported(msg: String) extends RuntimeException(msg)

object CFPlugin extends Plugin {

  val currentCFVersion = "2.1.8"

  lazy val cfPluginVersion = SettingKey[String]("Version of CFPlugin")

  lazy val cfJDKVersion = SettingKey[String]("Version of CF JDK")

  lazy val useAnnotatedJDK = SettingKey[Boolean]("Whether or not use annotated JDK")

  lazy val cfPlugin = config("cfPlugin")

  val cFPluginSettings = Seq(
  ) ++
    inConfig(cfPlugin)(Seq())

  def isCFJDKPath(path: String, cfVesion: String, jdkVersion: String): Boolean = {
    val cfPathMatch = path.contains("org.checkerframework") || path.contains("org/checkerframework")
    cfPathMatch && path.contains(cfVesion) && path.contains(jdkVersion)
  }

  def checkers: Seq[String] = Seq(
    "org.checkerframework.checker.nullness.NullnessChecker"
  )

  override lazy val settings = Seq(
    cfPluginVersion := currentCFVersion,
    useAnnotatedJDK := true,
    javacOptions ++= (if (useAnnotatedJDK.value) {
      for {
        attrFile <- (managedClasspath in Runtime).value
        path = attrFile.data.getAbsolutePath
        if isCFJDKPath(path, cfPluginVersion.value, cfJDKVersion.value)
      } yield "-Xbootclasspath/p:" + path
    } else {
      Seq.empty
    }),
    javacOptions ++= checkers.flatMap(c => Seq("-processor", c)),
    cfJDKVersion := (sys.props("java.specification.version") match {
      case "1.8" => "jdk8"
      case "1.7" => "jdk7"
      case s => throw new JDKVersionUnsupported(s"JDK version unsupported, found ${s}, required >= 1.7")
    }),
    libraryDependencies ++= Seq(
      "org.checkerframework" % "checker-qual" % cfPluginVersion.value,
      "org.checkerframework" % "checker" % cfPluginVersion.value,
      "org.checkerframework" % "compiler" % cfPluginVersion.value,
      "org.checkerframework" % cfJDKVersion.value % cfPluginVersion.value)
  ) ++ cFPluginSettings
}
