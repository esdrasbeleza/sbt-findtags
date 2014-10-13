package findtags

import sbt._
import Keys._
import scala.tools.nsc.io.File

object Plugin extends sbt.Plugin {

  def parseLines(file: File, possibleTags: Seq[String]) = {
    for {
      (content, lineNumber) <- file.lines.zipWithIndex
      tag <- possibleTags
      if content.contains(tag)
    } yield (lineNumber + 1, content)
  }

}
