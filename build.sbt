name := "xionnet"

version := "1.0-SNAPSHOT"

// Play plugin
lazy val root = (project in file(".")).enablePlugins(PlayJava)

// Ebean (ORM) plugin
lazy val myProject = (project in file(".")).enablePlugins(PlayJava, PlayEbean)

scalaVersion := "2.11.6"

libraryDependencies ++= Seq(
    javaJdbc,
    cache,
    javaWs,
    javaJpa,
    "mysql" % "mysql-connector-java" % "5.1.18"
)

// Play provides two styles of routers, one expects its actions to be injected, the
// other, legacy style, accesses its actions statically.
routesGenerator := InjectedRoutesGenerator
