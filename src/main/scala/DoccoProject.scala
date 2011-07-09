import sbt._

import Keys._

import util.matching.Regex

import ru.circumflex.{core, docco}
import core.cx
import docco._

/*!# SBT Circumflex Docco Plugin

This [sbt][sbt] plugin allows for docco style document
by running a single command in your build tool.

The plugin offers developers multiple ways to configure the
the output and docco generation. The program that actually
generates the docco style docs was written and currently
maintained by the [cicumflex][circumflex] team.

Simply insert the following line in your ./project/plugins/build.sbt:

    libraryDependencies += "com.github.philcali" %% "sbt-cx-docco" % "0.0.3"

Enjoy!

[sbt]: https://github.com/harrah/xsbt/wiki
[circumflex]: http://circumflex.ru/

*/
object CxDocco extends Plugin {
  val Docco = config("docco")

  val doccoBasePath = SettingKey[File]("base-path",
        "The base path for circumflex batch docco processing.")

  val doccoOutputPath = SettingKey[File]("output-path",
        "The output path resulting in html docco documentation.")

  val doccoPageTemplate = SettingKey[File]("page-template",
        "The page template applied to all converted source files.")

  val doccoIndexTemplate = SettingKey[File]("index-template",
        "The template for the index page in batch processing.")

  val doccoFilenameRegex = SettingKey[Regex]("filename-regex",
        "The regex used to obtain files for documenting.")
  
  val doccoTitle = SettingKey[String]("title",
        "The resulting index title")

  val doccoStripScaladoc = SettingKey[Boolean]("strip-scaladoc",
        "Strips the Scaladoc in the resulting docco output")

  val doccoSkipEmpty = SettingKey[Boolean]("skip-empty",
        "If true, filters docco's with no markdown.")

  lazy val doccoProperties = TaskKey[Unit]("properties",
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

  override lazy val settings = 
    doccoSettings ++ Seq (
      docco <<= (docco in Docco).identity
    ) ++ Seq(docco, doccoProperties).map (aggregate in _ := false)

  val doccoSettings = inConfig(Docco) (Seq (
    /*! ## Configurable Settings

  * doccoBasePath is where the crawler will start its search
  * doccoOutputPath is where you want your docco's
  * doccoPageTemplate is the path to the template producing this page
  * doccoIndexTemplate is the path to the template producing the previous page 
  * doccoFilenameRegex is the regex to filter physical files
  * doccoTitle is the title of the index page
  * doccoStripScaladoc strips any Scaladoc tags
  * doccoSkipEmpty will not include a file with documentation
    */
    // Configurable Settings
    doccoBasePath := file("."),
    doccoOutputPath <<= (target) { _ / "docco" },
    doccoPageTemplate := file(".") / "docco-batch-page.html.ftl",
    doccoIndexTemplate := file(".") / "docco-index.html.ftl",
    doccoFilenameRegex := """.*\.scala$""".r,
    doccoTitle <<= (name)( n => n),
    doccoStripScaladoc := true,
    doccoSkipEmpty := true,

    // Configurable tasks
    doccoProperties <<= doccoPropertiesTask,
    docco <<= (streams) map { s =>
      s.log.info("Generating docco's now...")
      val batch = new DoccoBatch
      batch.generate
      s.log.info("Done")
    },
    docco <<= docco dependsOn doccoProperties
  ))
}
