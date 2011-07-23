doccoTitle in CxDocco.Docco := "SBT Circumflex Docco Plugin"

name := "sbt-cx-docco"

organization := "com.github.philcali"

version <<= (sbtVersion) ("sbt" + _ + "_0.0.5")

sbtPlugin := true

libraryDependencies += "ru.circumflex" % "circumflex-docco" % "2.0.1"

publishTo := Some("Scala Tools Nexus" at 
                  "http://nexus.scala-tools.org/content/repositories/releases/")

credentials += Credentials(Path.userHome / ".ivy2" / ".credentials")
