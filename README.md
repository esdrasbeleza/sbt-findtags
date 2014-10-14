sbt-findtags
============

A sbt plugin to find tags in source files.

Not available yet
-----------------

This plugin is not available to download from any repository. If you want to try it, you can compile its source code and run `publishLocal` on sbt.


Adding to your project
----------------------

Add the following line to your `plugins.sbt` file:

```scala
addSbtPlugin("com.esdrasbeleza" % "sbt-findtags" % "0.1-SNAPSHOT")
```

Configuring
-----------

Until now, sbt-findtags has the following keys that can be added to build.sbt:

```scala
findtagsTags := Seq("TODO", "FIXME", "IMPROVEMENT")
```

Running
-------

Run the command `findtags` in sbt. The output will be the filenames that contain the found tags and their filenumber.
