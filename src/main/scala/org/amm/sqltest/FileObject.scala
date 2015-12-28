package org.amm.sqltest

import java.io.File

case class FileObject(file: File, query: QueryObject) {
  override def toString() = "file="+file+" query=[" + query+"]"
}
