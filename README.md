# SBT / Circumflex Docco plugin

This is an overly simple sbt plugin for [circumflex docco] style documentation generation.

Include the plugin in your `project/plugins/plugins.scala`

    import sbt._

    class Plugin(info: ProjectInfo) extends PluginDefinition(info) {
      val cxDoccoPlugin = "com.github.philcali" % "sbt-cx-docco" % "0.0.1"
    }

Your project definition may vary. The plugin allows pretty granular control over what
files to document, and where to place the documentation. The base `DoccoProject` requires
the `doccoCrawlerPath` to be overridden, using Sbt's `Path`'s. The default
`doccoOutputPath` will be the project's output path / "docco".

An example project definition below:

    import sbt._

    // A default project definition
    class Project(info: ProjectInfo) extends DefaultProject(info) with DoccoProject {
      def doccoCrawlerPath = mainSourcePath
    }

    // A parent project definition
    class Project(info: ProjectInfo) extends ParentProject(info) with DoccoProject {
      def doccoCrawlerPath = "."
    }

Being the `DefaultProject`s and `ParentProject`s are so common, there a trait for each one
which provides the aforementioned values as defaults: `DoccoSingle` and `DoccoParent`, respectively.

[circumflex docco]: http://circumflex.ru/projects/docco/index.html
