package com.sunway.foodordersystem.util

import scalikejdbc.*
import com.sunway.foodordersystem.model.{MenuItem, User}

trait Database:
  val derbyDriverClassname = "org.apache.derby.jdbc.EmbeddedDriver"
  val dbURL = "jdbc:derby:foodOrderDB;create=true;";
  // initialize JDBC driver & connection pool
  Class.forName(derbyDriverClassname)
  ConnectionPool.singleton(dbURL, "me", "mine")
  // ad-hoc session provider on the REPL
  given AutoSession = AutoSession

object Database extends Database:
  def setupDB(): Unit =
    if (!hasDBInitialize("Users"))
      User.initializeTable()
    if (!hasDBInitialize("Menu_Items"))
      MenuItem.initializeTable()

  def hasDBInitialize(tableName: String): Boolean =
    DB getTable tableName match
      case Some(x) => true
      case None => false
