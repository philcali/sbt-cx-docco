import sbt._

import Keys._

import util.matching.Regex

import ru.circumflex.{core, docco}
import core.cx
import docco._

object CxDocco extends Plugin {
  val doccoBasePath = SettingKey[File]("docco-base-path",
        "The base path for circumflex batch docco processing.")

  val doccoOutputPath = SettingKey[File]("docco-output-path",
        "The output path resulting in html docco documentation.")

  val doccoPageTemplate = SettingKey[File]("docco-page-template",
        "The page template applied to all converted source files.")

  val doccoIndexTemplate = SettingKey[File]("docco-index-template",
        "The template for the index page in batch processing.")

  val doccoFilenameRegex = SettingKey[Regex]("docco-filename-regex",
        "The regex used to obtain files for documenting.")
  
  val doccoTitle = SettingKey[String]("docco-title",
        "The resulting index title")

  val doccoStripScaladoc = SettingKey[Boolean]("docco-strip-scaladoc",
        "Strips the Scaladoc in the resulting docco output")

  val doccoSkipEmpty = SettingKey[Boolean]("docco-skip-empty",
        "If true, filters docco's with no markdown.")

  lazy val doccoProperties = TaskKey[Unit]("docco-properties",
        "Builds the cx.properties file for batch processing")
  private def doccoPropertiesTask = 
    (doccoBasePath, doccoOutputPath, doccoPageTemplate,
    doccoIndexTemplate, doccoFilenameRegex, doccoTitle, 
    doccoStripScaladoc, doccoSkipEmpty, streams) map {
    (bp, op, pt, it, fr, ti, ss, se, s)  =>
    // Settings circumflex properties
    cx("docco.basePath") = bp
    cx("docco.outputPath") = op

    // Need to build the output path
    if (!op.exists) {
      IO.createDirectory(op)
    }

    // The cx batch processor looks only for a string, so we must
    // verify that these exist
    if (pt.exists) {
      cx("docco.pageTemplate") = pt.absolutePath
    }

    if (it.exists) {
      cx("docco.indexTemplate") = it.absolutePath
    }

    cx("docco.filenameRegex") = fr.toString
    cx("docco.title") = ti
    cx("docco.stripScaladoc") = ss
    cx("docco.skipEmpty") = se
    
    s.log.info("Setting docco properties ... done.")
  }

  lazy val docco = TaskKey[Unit]("docco", "Docco style documentation generations")

  override lazy val settings = Seq (
    // Configurable Settings
    doccoBasePath := file("."),
    doccoOutputPath <<= (target) { _ / "docco" },
    doccoPageTemplate := file(".") / "docco-batch-page.html.ftl",
    doccoIndexTemplate := file(".") / "docco-index.html.ftl",
    doccoFilenameRegex := """.*\.scala$""".r,
    doccoTitle <<= (name)( n => n),
    doccoStripScaladoc := true,
    doccoSkipEmpty := false,

    // Configurable tasks
    doccoProperties <<= doccoPropertiesTask,
    docco <<= (streams) map { s =>
      s.log.info("Generating docco's now...")
      val batch = new DoccoBatch
      batch.generate
      s.log.info("Done")
    },
    docco <<= docco dependsOn doccoProperties
  )
}
