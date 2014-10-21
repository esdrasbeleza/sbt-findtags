sbt-findtags
============

A sbt plugin to find tags in source files.


Adding to your project
----------------------

Add the following line to your `plugins.sbt` file:

```scala
addSbtPlugin("com.esdrasbeleza" % "sbt-findtags" % "0.1-20141020")
```

Configuring
-----------

Until now, sbt-findtags has the following keys that can be added to build.sbt:

```scala
findtagsTags := Seq("TODO", "FIXME", "IMPROVEMENT")
```
The key above is optional and without it findtags will look for `TODO` and `FIXME`.

Running
-------

Run the command `findtags` in sbt. The output will be the filenames that contain the found tags and their filenumber.
