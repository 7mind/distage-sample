val V = new {
  val distage         = "0.10.3-M2"
  val logstage        = "0.10.3-M2"
  val scalatest       = "3.1.1"
  val scalacheck      = "1.14.3"
  val http4s          = "0.21.3"
  val zio             = "1.0.0-RC18-2"
  val zioCats         = "2.0.0.0-RC12"
  val kindProjector   = "0.11.0"
  val circeDerivation = "0.13.0-M4"
}

val Deps = new {
  val scalatest  = "org.scalatest" %% "scalatest" % V.scalatest
  val scalacheck = "org.scalacheck" %% "scalacheck" % V.scalacheck

  val distageCore            = "io.7mind.izumi" %% "distage-core" % V.distage
  val distageRoles           = "io.7mind.izumi" %% "distage-framework" % V.distage
  val distageDocker          = "io.7mind.izumi" %% "distage-framework-docker" % V.distage
  val distageTestkit         = "io.7mind.izumi" %% "distage-testkit-scalatest" % V.distage
  val fundamentalsReflection = "io.7mind.izumi" %% "fundamentals-reflection" % V.distage
  val logstageSlf4j          = "io.7mind.izumi" %% "logstage-adapter-slf4j" % V.logstage

  val http4sDsl    = "org.http4s" %% "http4s-dsl" % V.http4s
  val http4sServer = "org.http4s" %% "http4s-blaze-server" % V.http4s
  val http4sClient = "org.http4s" %% "http4s-blaze-client" % V.http4s
  val http4sCirce  = "org.http4s" %% "http4s-circe" % V.http4s

  val circeDerivation = "io.circe" %% "circe-derivation" % V.circeDerivation

  val kindProjector = "org.typelevel" % "kind-projector" % V.kindProjector cross CrossVersion.full

  val zio     = "dev.zio" %% "zio" % V.zio
  val zioCats = "dev.zio" %% "zio-interop-cats" % V.zioCats
}

inThisBuild(
  Seq(
    scalaVersion := "2.13.1",
    version      := "1.0.0-SNAPSHOT",
    organization := "io.7mind",
    run / fork   := true,
  )
)

lazy val leaderboard = project
  .in(file("."))
  .settings(
    name := "leaderboard",
    scalacOptions --= Seq("-Werror", "-Xfatal-warnings"),
    libraryDependencies ++= Seq(
      Deps.distageRoles,
      Deps.distageTestkit % Test,
      Deps.distageDocker % Test,
      Deps.scalatest % Test,
      "com.github.pureconfig" %% "pureconfig-magnolia" % "0.12.3",
      "com.propensive" %% "magnolia" % "0.14.5",
    ).map(_.exclude("org.scala-lang", "scala-reflect")) ++ Seq(
      Deps.fundamentalsReflection,
      Deps.logstageSlf4j,
      Deps.distageCore,
      Deps.scalacheck % Test,
      Deps.http4sDsl,
      Deps.http4sServer,
      Deps.http4sClient % Test,
      Deps.http4sCirce,
      Deps.circeDerivation,
      Deps.zio,
      Deps.zioCats,
    ).map(_.exclude("org.scala-lang", "scala-reflect")),
    addCompilerPlugin(Deps.kindProjector),
  )
