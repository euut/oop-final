package com.sunway.foodordersystem.util

import scala.util.{Success, Failure}
import com.sunway.foodordersystem.model.User

object Auth:
  def register(firstName: String, lastName: String, email: String, password: String): Either[String, User] =
    if User.isEmailExists(email) then
      Left("An account with this email already exists.")
    else
      val user = User(firstName, lastName, email, password)
      user.save() match
        case Success(id) =>
          user.id.value = id
          Right(user)
        case Failure(ex) =>
          Left(s"Registration failed: ${ex.getMessage}")

  def login(email: String, password: String): Either[String, User] =
    User.search(email) match
      case Some(user) if user.password.value == password =>
        Right(user)
      case Some(_) =>
        Left("Incorrect password.")
      case None =>
        Left("User not found.")
