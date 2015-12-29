package org.amm.sqltest

import java.io.File
import java.sql.DriverManager
import scala.io.Source
import org.testng.annotations._
import org.testng.Assert._

class EmbeddedTest {
  val dir = "src/test/resources"
  val driverClassName = "org.h2.Driver"
  val conn = DriverManager.getConnection("jdbc:h2:~/test", "sa", "")
  val stmt = conn.createStatement()

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
    assertTrue(SqlTestUtils.validate(conn,dir+"/success.sqt"))
  }

  @Test
  def test_Fail() {
    assertFalse(SqlTestUtils.validate(conn,dir+"/fail.sqt"))
  }
}
