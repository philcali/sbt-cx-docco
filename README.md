# SBT / Circumflex Docco plugin

This is an overly simple sbt plugin for [circumflex docco] style documentation generation.

Here's an example on this very plugin: [gh-pages][gh-pages]

Include the plugin in your `project/plugins/build.sbt`

    libraryDependencies += "com.github.philcali" %% "sbt-cx-docco" % "0.0.4"

Your project definition may vary. The plugin allows pretty granular control over what
files to document, and where to place the documentation. An example project definition below:

    doccoBasePath := file("src/main/scala")

    // I'm using this to filter files that contain this regex
    // This defaults to anything that ends with .scala of course
    doccoFilenameRegex := """.*\.[scala|sbt]""".r

Here's a list of all the circumflex settings during the batch docco process 
(I copied from the plugin's source):

    // Configurable Settings
    doccoBasePath := file("."),
    doccoOutputPath <<= (target) { _ / "docco" },
    doccoPageTemplate := file(".") / "docco-batch-page.html.ftl",
    doccoIndexTemplate := file(".") / "docco-index.html.ftl",
    doccoFilenameRegex := """.*\.scala$""".r,
    doccoTitle <<= (name)( n => n),
    doccoStripScaladoc := true,
    doccoSkipEmpty := true


Run the `docco` task to generate docco documentation.

[gh-pages]: http://philcali.github.com/sbt-cx-docco/
[circumflex docco]: http://circumflex.ru/projects/docco/index.html
