package com.sunway.foodordersystem.model

import com.sunway.foodordersystem.util.Database
import scalafx.beans.property.{ObjectProperty, StringProperty}
import scalikejdbc.{DB, scalikejdbcSQLInterpolationImplicitDef}

import scala.util.Try

class User(firstNameS: String, lastNameS: String) extends Database:
  val id        = ObjectProperty[Int](-1)
  val firstName = StringProperty(firstNameS)
  val lastName  = StringProperty(lastNameS)
  val email     = StringProperty("")
  val password  = StringProperty("")

  def save(): Try[Int] =
    if !isExist then
      Try {
        DB autoCommit { implicit session =>
          sql"""
            insert into users (firstName, lastName, email, password) values
            (${firstName.value}, ${lastName.value},
             ${email.value}, ${password.value})
          """.updateAndReturnGeneratedKey.apply().toInt
        }
      }
    else Try {
      DB autoCommit { implicit session =>
        sql"""
          update users set
            firstName = ${firstName.value},
            lastName  = ${lastName.value},
            email     = ${email.value},
            password  = ${password.value}
          where id    = ${id.value}
        """.update.apply()
      }
    }

  def delete(): Try[Int] =
    if isExist then
      Try {
        DB autoCommit { implicit session =>
          sql"delete from users where id = ${id.value}"
            .update
            .apply()
        }
      }
    else
      throw new Exception("User not found in database")

  def isExist: Boolean =
    DB readOnly { implicit session =>
      sql"select * from users where id = ${id.value}"
        .map(_.string("email"))
        .single
        .apply()
    } match
      case Some(x) => true
      case None => false


object User extends Database:
  def apply(
             firstNameS: String,
             lastNameS: String,
             emailS: String,
             passwordS: String,
             _id: Int = -1
           ): User =

    new User(firstNameS, lastNameS):
      email.value    = emailS
      password.value = passwordS
      id.value       = _id

  def initializeTable(): Unit =
    DB autoCommit { implicit session =>
      sql"""
          create table users (
            id INT GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1) NOT NULL,
            firstName varchar(64),
            lastName varchar(64),
            email varchar(100) unique,
            password varchar(100)
          )
        """.execute.apply()
    }

  def isEmailExists(email: String): Boolean =
    DB readOnly { implicit session =>
      sql"select 1 from users where email = $email"
        .map(_.int(1))
        .single
        .apply()
        .isDefined
    }

  def search(email: String): Option[User] =
    DB readOnly { implicit session =>
      sql"select * from users where email = $email"
        .map(rs =>
          User(
            rs.string("firstName"),
            rs.string("lastName"),
            rs.string("email"),
            rs.string("password"),
            rs.int("id")
          )
        )
        .single
        .apply()
    }