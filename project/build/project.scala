import sbt._

class Project(info: ProjectInfo) extends PluginProject(info) {
  def cxVerison = "0.3"
  val docco = "ru.circumflex" % "circumflex-docco" % cxVerison
}
