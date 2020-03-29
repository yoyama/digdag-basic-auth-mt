ThisBuild / scalaVersion     := scalaLangVersion
ThisBuild / version          := "0.1.0"
ThisBuild / organization     := "io.github.yoyama"
ThisBuild / organizationName := "yoyama"
ThisBuild / scalacOptions ++= Seq("-deprecation", "-feature")
ThisBuild / cancelable in Global := true

ThisBuild / resolvers ++= Seq(
  "central" at "https://repo1.maven.org/maven2",
  "jcenter" at "https://jcenter.bintray.com/",
  "digdag" at "https://dl.bintray.com/digdag/maven",
  "digdag-snapshots" at "https://dl.bintray.com/digdag/maven-snapshots",
)

lazy val airframeVersion = "20.3.1"
lazy val scalaLangVersion = "2.12.10"

lazy val root = (project in file("."))
  .settings(
    name := "digdag-basic-auth-mt",
    libraryDependencies ++= Seq(
      "org.scala-lang" % "scala-reflect" % scalaLangVersion,
      "org.scala-lang.modules" %% "scala-collection-compat" % "2.1.4",
      "io.digdag" % "digdag-spi" % "0.9.41" % "provided",
      "org.slf4j" % "slf4j-api" % "1.7.21" % "provided",
      "commons-codec" % "commons-codec" % "1.14",
      "org.wvlet.airframe" %% "airspec" % airframeVersion % "test"
    ),
    testFrameworks += new TestFramework("wvlet.airspec.Framework")
  )

