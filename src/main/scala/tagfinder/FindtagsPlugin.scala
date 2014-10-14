package findtags

import sbt._
import Keys._
import scala.io.Source

object FindtagsPlugin extends AutoPlugin {
  override def trigger = allRequirements

  object autoImport {
    val findtagsTask = taskKey[Seq[(String, Int, String)]]("Find tags in source files")
    val findtagsTags = settingKey[Seq[String]]("A list of tags that will be searched in source files, like TODO and FIXME")
  }

  import autoImport._

  override lazy val projectSettings = Seq(
    findtagsTags := Seq("TODO", "FIXME"),
    findtagsTask := {
      val fileList = (unmanagedSources in Compile).value
      val possibleTags = findtagsTags.value

      fileList.foldLeft[Seq[(String, Int, String)]](Nil){ (currentList, file) =>
        currentList ++ parseLines(file, possibleTags).toSeq
      }
    },
    commands += findtagsCommand
  )

  lazy val findtagsCommand = Command.command("findtags") { state =>
    val e = Project.extract(state)
    val (newState, foundTags) = e.runTask(findtagsTask, state)

    foundTags match {
      case Nil => println("No tags were found")
      case _ => {
        println(s"${foundTags.size} tags found:")
        foundTags.foreach { result =>
          val (name, lineNumber, content) = (result._1, result._2, result._3)
          println(s"$name:$lineNumber:$content\n")
        }
      }
    }

    newState
  }


  def parseLines(file: File, possibleTags: Seq[String]) = {
    for {
      (content, lineNumber) <- Source.fromFile(file).getLines.zipWithIndex
      tag <- possibleTags
      if content.contains(tag)
    } yield (file.getPath, lineNumber + 1, content)
  }

}
