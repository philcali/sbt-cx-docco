name := "sbt-cx-docco"

organization := "com.github.philcali"

version := "0.0.2"

sbtPlugin := true

libraryDependencies += "ru.circumflex" % "circumflex-docco" % "2.0.1"

publishTo := Some("Scala Tools Nexus" at 
                  "http://nexus.scala-tools.org/content/repositories/releases/")

credentials += Credentials(Path.userHome / ".ivy2" / ".credentials")
