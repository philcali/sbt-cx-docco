seq(doccoSettings: _*)

docco.title := "SBT Docco Plugin"

name := "sbt-cx-docco"

organization := "com.github.philcali"

version := "0.1.0"

sbtPlugin := true

libraryDependencies += "ru.circumflex" % "circumflex-docco" % "2.0.3"

publishTo := Some("Scala Tools Nexus" at 
                  "http://nexus.scala-tools.org/content/repositories/releases/")

credentials += Credentials(Path.userHome / ".ivy2" / ".credentials")
