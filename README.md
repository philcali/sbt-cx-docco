# SBT / Circumflex Docco plugin

This is an overly simple sbt plugin for [circumflex docco] style documentation generation.

Here's an example on this very plugin: [gh-pages][gh-pages]

Include the plugin in your `project/plugins.sbt`

``` scala
addSbtPlugin("com.github.philcali" % "sbt-cx-docco" % "0.1.2")
```

Your project definition may vary. The plugin allows pretty granular control over what
files to document, and where to place the documentation. An example project definition below:

## Build.sbt

``` scala
import com.github.philcali.DoccoPlugin.docco

seq(doccoSettings: _*)

docco.title := "My Cool Project Docco"

docco.basePath := file("src/main/scala")

// I'm using this to filter files that contain this regex
// This defaults to anything that ends with .scala of course
docco.filenameRegex := """.*\.[scala|sbt]""".r
```

## Build.scala

``` scala
import sbt._

import com.github.philcali.DoccoPlugin.{
  docco,
  doccoSettings
}

object MyBuild extends Build {
  lazy val project = Project(
    "cool-stuff",
    file("."),
    settings = Defaults.defaultSettings ++ doccoSettings ++ Seq(
      docco.title := "My Cool Project Docco",
      docco.basePath <<= sourceDirectory,
      docco.filenameRegex := """.*\.[scala|sbt]""".r
    )
  )
}
```

## Global Plugin

You can even include the plugin globally in `~/.sbt/plugins/build.sbt`,
and copy the dependency text from above.

## Settings and Tasks

Here's a list of all the circumflex settings during the batch docco process 
(I copied from the plugin's source):

``` scala
// Configurable Settings
docco.basePath := file("."),
docco.outputPath <<= (target) { _ / "docco" },
docco.pageTemplate := file(".") / "docco-batch-page.html.ftl",
docco.indexTemplate := file(".") / "docco-index.html.ftl",
docco.filenameRegex := """.*\.scala$""".r,
docco.title <<= name,  
docco.stripScaladoc := true,
docco.skipEmpty := true

// Configurable Tasks
docco.properties
docco.generate
```

Run the `docco-generate` task to generate docco documentation.

[gh-pages]: http://philcali.github.com/sbt-cx-docco/
[circumflex docco]: http://circumflex.ru/projects/docco/index.html
