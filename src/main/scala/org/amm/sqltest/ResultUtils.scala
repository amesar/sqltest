package org.amm.sqltest

import java.sql.ResultSet
import scala.collection.mutable.ListBuffer

object ResultUtils {

  def convertToResultSeq(rs: ResultSet) : ResultSeq = {
    val ncols = rs.getMetaData.getColumnCount
    val rseq = new ListBuffer[ColumnSeq]
    while(rs.next()) {
      val row = new ListBuffer[String]
      (0 until ncols).foreach(c => row.append(rs.getString(c+1)))
      rseq.append(row.toSeq)
    }
    rseq.toSeq
  }

  private val DELIMITER="|"
  private val NL="\n" 

  def convertToPipe(rseq: ResultSeq) : String = {
    val sbuf = new StringBuffer()
    rseq.zipWithIndex.foreach { case(row,r) => {
      row.zipWithIndex.foreach { case(col,c) => {
        val scol = (if (c==0) "" else DELIMITER) + col
        sbuf.append(scol)
      }}
      if (r < rseq.size-1) sbuf.append(NL)
    }}
    sbuf.toString
  }
}
