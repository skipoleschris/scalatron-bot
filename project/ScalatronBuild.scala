import sbt._
import Keys._
import sbtassembly.Plugin._
import AssemblyKeys._

object BuildSettings {
  val buildOrganization = "templemore"
  val buildScalaVersion = "2.9.1"
  val buildVersion      = "0.1"

  val buildSettings = Defaults.defaultSettings ++
                      Seq (organization := buildOrganization,
                           scalaVersion := buildScalaVersion,
                           version      := buildVersion) ++
                      ScctPlugin.scctSettings

  val assemblyOverrideSettings = Seq(assembleArtifact in packageScala := false,
                                     jarName in assembly := "ScalatronBot.jar",
                                     test in assembly := {})
}

object Dependencies {

  val extraResolvers = Seq("Typesafe Repository" at "http://repo.typesafe.com/typesafe/releases/")

  // General libraries
  val config = "com.typesafe.config" % "config" % "0.3.0" % "compile"
  val general = Seq(config)

  // Testing
  val specs2 = "org.specs2" %% "specs2" % "1.8.2" % "test"
  val testing = Seq(specs2)

  // Dependency groups
  val coreDeps = general ++ testing
}

object ScalatronBuild extends Build {
  import Dependencies._
  import BuildSettings._

  lazy val allSettings = buildSettings ++ assemblySettings ++
                         assemblyOverrideSettings ++ Seq(resolvers ++= extraResolvers, libraryDependencies ++= coreDeps)

  lazy val scalatronProject = Project("scalatron", file("."), settings = allSettings)
}
