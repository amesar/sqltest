package org.amm.sqltest

import java.io.File
import scala.io.Source
import scala.collection.mutable.ListBuffer

class SqlTestFileReader {
  private val NL = "\n"
  private val DELIMITER = "\\|" 

  trait Section
  case object QUERY extends Section
  case object RESULT extends Section
  case object NONE extends Section

  def readResultFile(file: File) : FileObject = {
    var section: Section = NONE
    val sbuf = new StringBuilder()
    var squery: String = null
    val rseq = new ListBuffer[ColumnSeq]()

    val lines = Source.fromFile(file).getLines.toList
    for (line <- lines) {
      if (line.equals("#query")) {
        section = QUERY
      } else if (section==QUERY && isContent(line)) {
        sbuf.append(line)
      } else if (line.equals("#result")) {
        section = RESULT
        squery = sbuf.toString
      } else if (section==RESULT && isContent(line)) {
        sbuf.append(line+NL)
        rseq.append(line.split(DELIMITER).toSeq)
      }
    }
    FileObject(file, QueryObject(file.getName,squery,rseq))
  }

  def isBlank(str: String) = str.trim.length==0
  def isContent(line: String) = !line.startsWith("#") && !isBlank(line)
}
