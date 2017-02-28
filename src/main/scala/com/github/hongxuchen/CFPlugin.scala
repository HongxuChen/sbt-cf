package com.github.hongxuchen

import sbt.Keys._
import sbt._

object ScopedCFPlugin extends Plugin {

  private def doCommand(state: State): State = {
    val log = state.log
    val extracted: Extracted = Project.extract(state)
    val structure = extracted.structure
    val projectRefs = structure.allProjectRefs
    state
  }

  lazy val cfPlugin = config("cfPlugin")

  lazy val cfPluginVersion = SettingKey[String]("Version of CFPlugin")

  val cFPluginSettings = Seq(
    cfPluginVersion := "0.0.1"
  ) ++
  inConfig(cfPlugin)(Seq(
  ))

  override lazy val settings = Seq(
    commands ++= Seq(sample)
  )

  lazy val sample = Command.command("sampleCommand") { state =>
    println("Hello SBT World!")
    state
  }
}
