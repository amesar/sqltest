package org.amm.sqltest

case class QueryObject(name: String, query: String, result: ResultSeq) {
  override def toString = query
}
