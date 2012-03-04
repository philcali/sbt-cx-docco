package com.github.philcali

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

    addSbtPlugin("com.github.philcali" % "sbt-cx-docco" % "0.1.2")

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

    val generate = TaskKey[Unit]("docco-generate", "Docco style documentation generations")
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

  lazy val doccoSettings: Seq[Setting[_]] = Seq (
    /*! ## Configurable Settings

  * docco.basePath is where the crawler will start its search
  * docco.outputPath is where you want your docco's
  * docco.pageTemplate is the path to the template producing this page
  * docco.indexTemplate is the path to the template producing the previous page 
  * docco.filenameRegex is the regex to filter physical files
  * docco.title is the title of the index page
  * docco.stripScaladoc strips any Scaladoc tags
  * docco.skipEmpty will not include a file without documentation
  * docco.properties sets the global docco property values for generation
  * docco.generate run the actual docco generation
  * docco.useScaladoc uses Scaladoc to generate Docco documentation
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
    properties <<= doccoPropertiesTask dependsOn doccoMorePropertiesTask,

    generate <<= (streams) map { s =>
      s.log.info("Generating docco's now...")
      val batch = new DoccoBatch
      batch.generate
      s.log.info("Done")
    },
    generate <<= generate dependsOn properties,

    cleanFiles <+= outputPath,

    aggregate in properties := false,
    aggregate in generate := false
  )
}
