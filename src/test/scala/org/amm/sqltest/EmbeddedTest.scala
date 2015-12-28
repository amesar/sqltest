package org.amm.sqltest

import java.io.File
import java.sql.{DriverManager,Statement}
import scala.io.Source
import org.testng.annotations._
import org.testng.Assert._

class EmbeddedTest {
  val dir = "src/test/resources"
  val driverClassName = "org.h2.Driver"
  val conn = DriverManager.getConnection("jdbc:h2:~/test", "sa", "")
  val stmt = conn.createStatement()
  val reader = new SqlTestFileReader()

  @BeforeClass
  def beforeClass() {
    val ddlDrop = Source.fromFile(dir+"/drop.ddl").mkString
    stmt.executeUpdate(ddlDrop)

    val ddlCreate = Source.fromFile(dir+"/create.ddl").mkString
    stmt.executeUpdate(ddlCreate)

    for (line <- Source.fromFile(dir+"/insert.sql").getLines()) stmt.execute(line)
  }

  @Test
  def test_Success() {
    assertTrue(compare(dir+"/success.sqt"))
  }

  @Test
  def test_Fail() {
    assertFalse(compare(dir+"/fail.sqt"))
  }

  def compare(file: String) : Boolean = {
    val fobj = reader.readResultFile(new File(file))
    val rseqFromFile = fobj.query.result
    val rseqFromExe = execute(stmt,fobj.query.query)
    rseqFromFile.equals(rseqFromExe)
  }

  def execute(stmt: Statement, query: String) : ResultSeq  = {
    val rs = stmt.executeQuery(query)
    ResultUtils.convertToResultSeq(rs)
  }
}
