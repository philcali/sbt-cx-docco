import sbt._

import ru.circumflex.docco._

// Single project will use the source and sub packages
trait DoccoSingle extends DefaultProject with DoccoProject {
  def doccoCrawlerPath = mainSourcePath
}

// Parents will naturally grab all the children
trait DoccoParent extends ParentProject with DoccoProject {
  def doccoCrawlerPath = "."
}

// Project(info: ProjectInfo) extends DefaultProject(info) 
//                               with DoccoSingle 
//                               with DropboxSourcePublish
trait DropboxSourcePublish extends DoccoProject {
  // Common Dropbox location
  override def doccoOutputPath = Path.userHome / "Dropbox" / "Public" / projectName.value
}

// Perfect legal just to implement this one
trait DoccoProject extends BasicManagedProject {
  def doccoCrawlerPath: Path 
  def doccoOutputPath = outputPath / "docco"

  lazy val docco = task {
    try {
      // Bulk of the work already done
      val batch = new DoccoBatch(doccoCrawlerPath.asFile, doccoOutputPath.asFile)
      batch.generate()
      None
    } catch {
      case e: Exception => Some(e.getMessage)
    }
  } describedAs("Docco style documentation generation")
}
