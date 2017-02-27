package com.github.hongxuchen

import sbt._
import sbt.Keys._

object Sbtcf extends Plugin {
  override lazy val settings = Seq(
    commands ++= Seq(
      sample
    )
  )

  lazy val sample = Command.command("sampleCommand") { state =>
    println("Hello SBT World!")
    state
  }
}

