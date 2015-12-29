package org.amm.sqltest

import java.io._
import java.sql.{DriverManager,Statement}

object GenerateDriver {
  private val NL = "\n"

  def main(args: Array[String]) {
    if (args.length < 3) {
      println("ERROR: Expecting INPUT_FILE, DRIVER and URL")
      return
    }

    val ifile = new File(args(0))
    val driverClassName= args(1)
    val url = args(2)
    Class.forName(driverClassName)
    val conn = DriverManager.getConnection(url)

    val stmt = conn.createStatement()

    val odir = "out"
    (new File(odir)).mkdirs()
    val ofile = odir + "/" + ifile.getName
    println("ifile="+ifile+" ofile="+ofile)

    val qobj = SqlTestUtils.readFile(ifile)

    val rs = stmt.executeQuery(qobj.query)
    val rseq = ResultUtils.convertToResultSeq(rs)

    new PrintWriter(ofile) { 
      write("#query"+NL)
      write(qobj.query+NL)
      write(NL)
      write("#result"+NL)
      write(ResultUtils.convertToPipe(rseq)+NL)
      close()
    }
  }
}
