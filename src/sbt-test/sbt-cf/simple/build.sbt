name:="sbt-cf-simple"
version:="0.0.1"
scalaVersion := "2.11.8"

crossPaths := false
autoScalaLibrary := true

cfPluginVersion := "2.1.7"
cfUseAnnotatedJDK := true
(cfJDKVersion in cfPlugin) := "jdk8"
cfCheckers := Seq(
  "Nullness",
  "FmtStr"
)

antlr4Settings