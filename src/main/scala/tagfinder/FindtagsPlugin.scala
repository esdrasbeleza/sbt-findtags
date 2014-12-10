package sbtfindtags

import sbt._
import Keys._
import scala.io.Source

object FindtagsPlugin extends AutoPlugin {
  override def trigger = allRequirements

  object autoImport {
    val findtags = taskKey[Unit]("Find tags in source files")
    val findtagsTags = settingKey[Seq[String]]("A list of tags that will be searched in source files, like TODO and FIXME")
    val findtagsFailsIfTagsAreFound = settingKey[Boolean]("Consider tags as a failure.")
  }

  import autoImport._

  override lazy val projectSettings = Seq(
    findtagsTags := Seq("TODO", "FIXME"),
    findtagsFailsIfTagsAreFound := false,
    findtags := {
      val fileList = (unmanagedSources in Compile).value
      val possibleTags = findtagsTags.value

     val foundTags = fileList.foldLeft[Seq[(String, Int, String)]](Nil){ (currentList, file) =>
        currentList ++ parseLines(file, possibleTags).toSeq
      }

      foundTags match {
        case Nil => println("No tags were found")
        case _ => {
          val outputFile = target.value / "findtags/output.txt"
          if (outputFile.exists()) { outputFile.delete() }

          println(s"${foundTags.size} tags found:")
          foundTags.foreach { result =>
            val (name, lineNumber, content) = (result._1, result._2, result._3)
            val output = s"$name:$lineNumber:$content".trim
            IO.write(outputFile, output)
            println(output)
          }

          if (findtagsFailsIfTagsAreFound.value) {
            sys.error(s"Failing, ${foundTags.size} tags were found")
          }
        }
      }
    }
  )


  def parseLines(file: File, possibleTags: Seq[String]) = {
    for {
      (content, lineNumber) <- Source.fromFile(file).getLines.zipWithIndex
      tag <- possibleTags
      if content.contains(tag)
    } yield (file.getPath, lineNumber + 1, content)
  }

}
