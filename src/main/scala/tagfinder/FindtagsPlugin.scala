package sbtfindtags

import sbt._
import Keys._
import scala.io.Source

object FindtagsPlugin extends AutoPlugin {
  override def trigger = allRequirements

  object autoImport {
    val findtags = taskKey[Unit]("Find tags in source files")

    object FindTagsKeys {
      lazy val tagList = settingKey[Seq[String]]("A list of tags that will be searched in source files, like TODO and FIXME")
      lazy val strict = settingKey[Boolean]("Consider tags as a failure.")
    }

    lazy val baseFindtagsSettings: Seq[Def.Setting[_]] = Seq(
      findtags := {
        FindTags((FindTagsKeys.tagList in findtags).value,
                  (FindTagsKeys.strict in findtags).value,
                  (unmanagedSources in findtags).value,
                  (baseDirectory in findtags).value,
                  (target.value / "findtags/output.txt"),
                  streams.value.log)
      },
      FindTagsKeys.tagList in findtags := Seq("TODO", "FIXME"),
      FindTagsKeys.strict in findtags := false,
      compile <<= (compile in Compile) dependsOn findtags
   )
  }

  import autoImport._



  override lazy val projectSettings =
      inConfig(Compile)(baseFindtagsSettings)
}

object FindTags {
  def apply(possibleTags: Seq[String], 
            strict: Boolean, 
            fileList: Seq[File], 
            projectDirectory: File,
            outputFile: File,
            log: Logger) = {
      val mergedTags = possibleTags.mkString(", ")
      log.info(s"Looking for tags (${mergedTags}):")
     
      val tagsFound = fileList.foldLeft[Seq[(String, Int, String)]](Nil){ (currentList, file) =>
        currentList ++ parseLines(file, possibleTags, projectDirectory).toSeq
      }

      tagsFound match {
        case Nil => log.info("No tags were found")
        case _ => {
          if (outputFile.exists()) { outputFile.delete() }

          log.info(s"${tagsFound.size} tags found:")
          tagsFound.foreach { result =>
            val (name, lineNumber, content) = (result._1, result._2, result._3)
            val output = s"$name:$lineNumber:$content".trim
            IO.write(outputFile, output)
            log.info(output)
          }

          if (strict) {
            log.error(s"Failing, ${tagsFound.size} tags were found.")
            log.error("sbt-findtags was configured to fail if tags are found. Fix your project or change findtagsFailsIfTagsAreFound to false in your build configuration to get rid of this message.")
          }
        }
      }
    }

  def parseLines(file: File, possibleTags: Seq[String], projectDirectory: File) = {
    for {
      (content, lineNumber) <- Source.fromFile(file).getLines.zipWithIndex
      tag <- possibleTags
      if content.contains(tag)
    } yield (IO.relativize(projectDirectory, file).getOrElse(file.getPath), lineNumber + 1, content)
  }
}
