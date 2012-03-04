# SBT / Circumflex Docco plugin

This is an overly simple sbt plugin for [circumflex docco] style documentation generation.

Here's an example on this very plugin: [gh-pages][gh-pages]

Include the plugin in your `project/plugins/build.sbt`

    libraryDependencies <+= (sbtVersion) ( sv =>
      "com.github.philcali" %% "sbt-cx-docco" % ("sbt" + sv + "_0.0.5")
    )

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
    doccoTitle <<= name.identity,  
    doccoStripScaladoc := true,
    doccoSkipEmpty := true


Run the `docco` task to generate docco documentation.

## Hacking

 * See <https://github.com/inca/circumflex> for modifying the Docco source.

 * Hack, hack, hack

 * Run `mvn clean install -pl circumflex-docco -am`

 * Add the following line to `build.sbt` 

    resolvers += "Local Maven Repository" at "file://"+Path.userHome+"/.m2/repository"

*then...*

 * Run `sbt docco`

*or...*

 * Run `sbt publish-local`

 * In your own project add `changing()` to the end of the dependency in your `plugins.sbt`

[gh-pages]: http://philcali.github.com/sbt-cx-docco/
[circumflex docco]: http://circumflex.ru/projects/docco/index.html
