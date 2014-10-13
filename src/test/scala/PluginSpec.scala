package tagfinder.tests

import findtags.Plugin
import org.scalatest._
import org.scalatest.mock.MockitoSugar
import scala.tools.nsc.io.File
import org.mockito.Mockito._

class PluginSpec extends FlatSpec with MockitoSugar with Matchers {
  val fakeLines = List("Some line containing TODO to our tests", "Some common line", "Some line containing FIXME")

  it should "return lines containing tags" in {
    val file = mock[File]
    when(file.lines()).thenReturn(fakeLines.toIterator)

    val lines = Plugin.parseLines(file, Seq("TODO", "FIXME")).toList
    lines should contain (1, fakeLines(0))
    lines should contain (3, fakeLines(2))
  }



}