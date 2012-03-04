import sbt._

import Keys._

import util.matching.Regex

import ru.circumflex.core.cx
import ru.circumflex.docco._

/*!# SBT Circumflex Docco Plugin

This [sbt][sbt] plugin allows for docco style document
by running a single command in your build tool.

The plugin offers developers multiple ways to configure the
the output and docco generation. The program that actually
generates the docco style docs was written and currently
maintained by the [cicumflex][circumflex] team.

Simply insert the following lines in your ./project/plugins/build.sbt:

    addSbtPlugin("com.github.philcali" % "sbt-lwjgl-plugin" % "0.1.0")

Enjoy!

[sbt]: https://github.com/harrah/xsbt/wiki
[circumflex]: http://circumflex.ru/

*/
object DoccoPlugin extends Plugin {
  import docco._

  object docco {
    val basePath = SettingKey[File]("docco-base-path",
          "The base path for circumflex batch docco processing.")

    val outputPath = SettingKey[File]("docco-output-path",
          "The output path resulting in html docco documentation.")

    val pageTemplate = SettingKey[File]("docco-page-template",
          "The page template applied to all converted source files.")

    val indexTemplate = SettingKey[File]("docco-index-template",
          "The template for the index page in batch processing.")

    val filenameRegex = SettingKey[Regex]("docco-filename-regex",
          "The regex used to obtain files for documenting.")
    
    val title = SettingKey[String]("docco-title",
          "The resulting index title")

    val stripScaladoc = SettingKey[Boolean]("docco-strip-scaladoc",
          "Strips the Scaladoc in the resulting docco output")

    val skipEmpty = SettingKey[Boolean]("docco-skip-empty",
          "If true, filters docco's with no markdown.")

    val useScaladoc = SettingKey[Boolean]("docco-use-scaladoc",
          "Uses Scaladoc to generate Docco documentation")

    val properties = TaskKey[Unit]("docco-properties",
          "Builds the cx.properties file for batch processing")
  }

  /**
    * This is some ScalaDoc
    */
  private def doccoMorePropertiesTask = (useScaladoc) map {
    (us) =>
      cx("docco.useScaladoc") = us
  }

  private def doccoPropertiesTask = 
    (basePath, outputPath, pageTemplate,
    indexTemplate, filenameRegex, title, 
    stripScaladoc, skipEmpty, streams) map {
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

  lazy val doccoTask = TaskKey[Unit]("docco", "Docco style documentation generations")

  lazy val doccoSettings: Seq[Setting[_]] = Seq (
    /*! ## Configurable Settings

  * doccoBasePath is where the crawler will start its search
  * doccoOutputPath is where you want your docco's
  * doccoPageTemplate is the path to the template producing this page
  * doccoIndexTemplate is the path to the template producing the previous page 
  * doccoFilenameRegex is the regex to filter physical files
  * doccoTitle is the title of the index page
  * doccoStripScaladoc strips any Scaladoc tags
  * doccoSkipEmpty will not include a file with documentation
  * useScaladoc uses Scaladoc to generate Docco documentation
    */
    // Configurable Settings
    basePath := file("."),
    outputPath <<= (target) { _ / "docco" },
    pageTemplate := file(".") / "docco-batch-page.html.ftl",
    indexTemplate := file(".") / "docco-index.html.ftl",
    filenameRegex := """.*\.scala$""".r,
    docco.title <<= name,
    stripScaladoc := true,
    skipEmpty := true,
    useScaladoc := false,

    // Configurable tasks
    properties <<= doccoPropertiesTask,
    properties <<= doccoPropertiesTask dependsOn doccoMorePropertiesTask,

    doccoTask <<= (streams) map { s =>
      s.log.info("Generating docco's now...")
      val batch = new DoccoBatch
      batch.generate
      s.log.info("Done")
    },
    doccoTask <<= doccoTask dependsOn properties,

    aggregate in properties := false,
    aggregate in doccoTask := false
  )
}
