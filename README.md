sbt-findtags
============

A sbt plugin to find tags in source files, like `TODO` and `FIXME`. You can use
it to keep track of your tags and your technical debt.


Adding to your project
----------------------

Add the following line to your `plugins.sbt` file:

```scala
addSbtPlugin("com.esdrasbeleza" % "sbt-findtags" % "0.3")
```

Configuring
-----------

In your configuration file, import the sbt-findtags keys:

```scala
import sbtfindtags.FindtagsPlugin._
```

`findtagsTags` set which keys must be searched. The default values are `TODO` 
and `FIXME`.

```scala
findtagsTags := Seq("TODO", "FIXME", "IMPROVEMENT")
```

`findtagsFailsIfTagsAreFound`, if set to `true`, considers that if some tag is 
found then it must be an error. The default value is `false`.

```scala
findtagsFailsIfTagsAreFound := true
```

This is specially useful if you want your build to break if we have any tags. To
achieve that, add the following line and so sbt-findtags will run before your 
code compiles:

```scala
compile <<= (compile in Compile) dependsOn findtags
```

Running
-------

Run the command `findtags` in sbt. The output will be the filenames that contain
the found tags and their filenumber. They will also be available in 
`buildDir/findtags/output.txt`.

