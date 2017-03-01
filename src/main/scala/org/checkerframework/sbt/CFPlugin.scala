package org.checkerframework.sbt

import sbt.Keys._
import sbt._

class UnsupportedJDK(msg: String) extends RuntimeException(msg)

object CheckerUtils {
  // TODO might use reflection
  val name2Checker = Map(
    "Nullness" -> "nullness.NullnessChecker",
    "Interning" -> "interning.InterningChecker",
    "Lock" -> "lock.LockChecker",
    "Fenum" -> "fenum.FenumChecker",
    //    "Index" -> "index.IndexChecker", // TODO
    "Tainting" -> "tainting.TaintingChecker",
    "Regex" -> "regex.RegexChecker",
    "FmtStr" -> "formatter.FormatterChecker",
    "I18nFmtStr" -> "i18nformatter.I18nFormatChecker",
    "Propkey" -> "propkey.PropertyKeyChecker",
    "I18n" -> "i18n.I18nChecker",
    "Signature" -> "signature.SignatureChecker",
    "GUIEffect" -> "guieffect.GuiEffectChecker",
    "Units" -> "units.UnitsChecker",
    "Signedness" -> "signedness.SignednessChecker",
    "Aliasing" -> "aliasing.AliasingChecker",
    "Linear" -> "linear.LinearChecker",
    "Subtyping" -> "subtyping.SubtypingChecker"
  ) map { case (k, v) => k -> s"org.checkerframework.checker.${v}" }

  def builtinCNames: Seq[String] = name2Checker.keys.toSeq

  // TODO deal with customized checkers

}

object CFPlugin extends Plugin {

  import CheckerUtils._

  val currentCFVersion = "2.1.8"

  lazy val cfPluginVersion = SettingKey[String]("Version of Checker Framework")

  lazy val cfJDKVersion = SettingKey[String]("Version of CF JDK")

  lazy val cfUseAnnotatedJDK = SettingKey[Boolean]("Whether or not use annotated JDK")

  lazy val cfPlugin = config("cfPlugin")

  // TODO safer way
  def isCFJDKPath(path: String, cfVesion: String, jdkVersion: String): Boolean = {
    val cfPathMatch = List(".", "/", "\\").map(List("org", "checkerframework").mkString).exists(path.contains)
    cfPathMatch && path.contains(cfVesion) && path.contains(jdkVersion)
  }

  lazy val cfCheckers = SettingKey[Seq[String]]("the checkers used in the compilation")

  // TODO deal with jdk mismatch "-source/-target"
  override lazy val settings = inConfig(cfPlugin)(Seq(
    cfJDKVersion := (sys.props("java.specification.version") match {
      case "1.8" => "jdk8"
      case "1.7" => "jdk7"
      case s => throw new UnsupportedJDK(s"JDK version unsupported, found ${s}, required >= 1.7")
    })
  )) ++ Seq(
    cfCheckers := builtinCNames,
    cfPluginVersion := currentCFVersion,
    cfUseAnnotatedJDK := true,
    javacOptions ++= (if (cfUseAnnotatedJDK.value) {
      for {
        attrFile <- (managedClasspath in Runtime).value
        path = attrFile.data.getAbsolutePath
        if isCFJDKPath(path, cfPluginVersion.value, (cfJDKVersion in cfPlugin).value)
      } yield "-Xbootclasspath/p:" + path
    } else {
      Seq.empty
    }),
    javacOptions += "-AprintErrorStack",
    javacOptions ++= cfCheckers.value.flatMap(c => Seq("-processor", name2Checker(c))),
    libraryDependencies ++= Seq(
      "org.checkerframework" % "checker-qual" % cfPluginVersion.value,
      "org.checkerframework" % "checker" % cfPluginVersion.value,
      "org.checkerframework" % "compiler" % cfPluginVersion.value,
      "org.checkerframework" % (cfJDKVersion in cfPlugin).value % cfPluginVersion.value)
  )
}
