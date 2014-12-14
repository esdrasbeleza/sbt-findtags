package sbtfindtags

import sbt._
import Keys._
import scala.io.Source

object FindtagsPlugin extends AutoPlugin {
  override def trigger = allRequirements

  object autoImport {
    val findtags = taskKey[Unit]("Find tags in source files")

    object FindTags {
      val tags = settingKey[Seq[String]]("A list of tags that will be searched in source files, like TODO and FIXME")
      val failOnTags = settingKey[Boolean]("Consider tags as a failure.")
    }
  }


  import autoImport._

  override lazy val projectSettings = Seq(
    FindTags.tags := Seq("TODO", "FIXME"),
    FindTags.failOnTags := false,

    findtags := {
      val log = streams.value.log

      log.info("Starting find tags:")

      val fileList = (unmanagedSources in Compile).value
      val projectDirectory = (baseDirectory in Compile).value
      val possibleTags = findtagsTags.value

      val tagsFound = fileList.foldLeft[Seq[(String, Int, String)]](Nil){ (currentList, file) =>
        currentList ++ parseLines(file, possibleTags, projectDirectory).toSeq
      }

      tagsFound match {
        case Nil => log.info("No tags were found")
        case _ => {
          val outputFile = target.value / "findtags/output.txt"
          if (outputFile.exists()) { outputFile.delete() }

          log.info(s"${tagsFound.size} tags found:")
          tagsFound.foreach { result =>
            val (name, lineNumber, content) = (result._1, result._2, result._3)
            val output = s"$name:$lineNumber:$content".trim
            IO.write(outputFile, output)
            log.info(output)
          }

          if (findtagsFailsIfTagsAreFound.value) {
            log.error(s"Failing, ${tagsFound.size} tags were found.")
            log.error("sbt-findtags was configured to fail if tags are found. Fix your project or change findtagsFailsIfTagsAreFound to false in your build configuration to get rid of this message.")
          }
        }
      }
    },
    compile <<= (compile in Compile) dependsOn findtags
  )

  def parseLines(file: File, possibleTags: Seq[String], projectDirectory: File) = {
    for {
      (content, lineNumber) <- Source.fromFile(file).getLines.zipWithIndex
      tag <- possibleTags
      if content.contains(tag)
    } yield (IO.relativize(projectDirectory, file).getOrElse(file.getPath), lineNumber + 1, content)
  }

}
