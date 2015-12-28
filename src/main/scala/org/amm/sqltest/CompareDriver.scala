package org.amm.sqltest

import java.io._
import java.sql.{DriverManager,Statement}

object CompareDriver {

  def main(args: Array[String]) {
    if (args.length < 3) {
      println("ERROR: Expecting FILE, DRIVER and URL")
      return
    }

    val file = new File(args(0))
    val driverClassName= args(1)
    val url = args(2)

    Class.forName(driverClassName)
    val conn = DriverManager.getConnection(url)
    val stmt = conn.createStatement()

    println("file="+file)

    val reader = new SqlTestFileReader()
    val fobj = reader.readResultFile(file)
    dump(fobj)

    val resultFromFile = fobj.query.result
    val resultFromExe = execute(stmt,fobj.query.query)
    val res = resultFromFile.equals(resultFromExe)

    dump(resultFromExe,"EXECUTE")
    println("\nEQUALS: "+res)

    println()
  }

  def execute(stmt: Statement, query: String) : ResultSeq  = {
    val rs = stmt.executeQuery(query)
    ResultUtils.convertToResultSeq(rs)
  }

  def dump(fobj: FileObject) {
    def qobj = fobj.query
    println("==== QUERY")
    println(qobj.query)
    dump(qobj.result,"FILE")
  }

  def dump(rseq: ResultSeq, msg: String) {
    println("==== "+msg)
    println(ResultUtils.convertToPipe(rseq))
  }
}
