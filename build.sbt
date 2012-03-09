import com.github.philcali.DoccoPlugin.docco

seq(doccoSettings: _*)

docco.title := "SBT Docco Plugin"

name := "sbt-cx-docco"

organization := "com.github.philcali"

version := "0.1.3-SNAPSHOT"

sbtPlugin := true

scalacOptions += "-deprecation"

libraryDependencies += "ru.circumflex" % "circumflex-docco" % "2.1"

publishTo <<= version { v =>
  val nexus = "https://oss.sonatype.org/"
  if (v.trim.endsWith("SNAPSHOT"))
    Some("snapshots" at nexus + "content/repositories/snapshots")
  else
    Some("releases" at nexus + "service/local/staging/deploy/maven2")
}

publishMavenStyle := true

publishArtifact in Test := false

pomIncludeRepository := { x => false }

pomExtra := (
  <url>https://github.com/philcali/sbt-cx-docco</url>
  <licenses>
    <license>
      <name>The MIT License</name>
      <url>http://www.opensource.org/licenses/mit-license.php</url>
      <distribution>repo</distribution>
    </license>
  </licenses>
  <scm>
    <url>git@github.com:philcali/sbt-cx-docco.git</url>
    <connection>scm:git:git@github.com:philcali/sbt-cx-docco.git</connection>
  </scm>
  <developers>
    <developer>
      <id>philcali</id>
      <name>Philip Cali</name>
      <url>http://philcalicode.blogspot.com/</url>
    </developer>
  </developers>
)
