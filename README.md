# SBT / Circumflex Docco plugin

This is an overly simple sbt plugin for [circumflex docco] style documentation generation.

Here's an example on this very plugin: [gh-pages][gh-pages]

Include the plugin in your `project/plugins.sbt`

``` scala
addSbtPlugin("com.github.philcali" % "sbt-cx-docco" % "0.1.0")
```

Your project definition may vary. The plugin allows pretty granular control over what
files to document, and where to place the documentation. An example project definition below:

``` scala
seq(doccoSettings: _*)

docco.title := "My Cool Project Docco"

docco.basePath := file("src/main/scala")

// I'm using this to filter files that contain this regex
// This defaults to anything that ends with .scala of course
docco.filenameRegex := """.*\.[scala|sbt]""".r
```

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
```

Run the `docco` task to generate docco documentation.

[gh-pages]: http://philcali.github.com/sbt-cx-docco/
[circumflex docco]: http://circumflex.ru/projects/docco/index.html
