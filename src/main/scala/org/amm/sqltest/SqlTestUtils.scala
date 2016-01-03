package org.amm.sqltest

import java.io.File
import java.sql.Connection
import scala.io.Source
import scala.collection.mutable.ListBuffer

object SqlTestUtils {
  private val NL = "\n"
  private val DELIMITER = "\\|" 

  trait Section
  case object QUERY extends Section
  case object RESULT extends Section
  case object NONE extends Section

  def readFile(filename: String) : QueryObject = readFile(new File(filename))

  def readFile(file: File) : QueryObject = {
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
    QueryObject(file.getName,squery,rseq)
  }

  def validate(conn: Connection, filename: String, verbose: Boolean=true) : Boolean = {
    val stmt = conn.createStatement()
    val qobj = SqlTestUtils.readFile(filename)
    if (verbose) dump(qobj)

    val rseqFromFile = qobj.result
    val rs = stmt.executeQuery(qobj.query)
    val rseqFromExe = ResultUtils.convertToResultSeq(rs)
    if (verbose) dump(rseqFromExe,"EXECUTE #results="+rseqFromExe.size)
    stmt.close()
    rseqFromFile.equals(rseqFromExe)
  }

  def dump(qobj: QueryObject) {
    println("==== QUERY")
    println(qobj.query)
    dump(qobj.result,"FILE #results="+qobj.result.size)
  }

  def dump(rseq: ResultSeq, msg: String) {
    println("==== "+msg)
    println(ResultUtils.convertToPipe(rseq))
  }

  private def isBlank(str: String) = str.trim.length==0
  private def isContent(line: String) = !line.startsWith("#") && !isBlank(line)
}
