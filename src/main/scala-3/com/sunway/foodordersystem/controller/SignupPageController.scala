package com.sunway.foodordersystem.controller

import com.sunway.foodordersystem.MainApp
import com.sunway.foodordersystem.util.Auth
import javafx.event.ActionEvent
import javafx.fxml.FXML
import javafx.scene.control.{PasswordField, TextField}
import scalafx.scene.control.Alert.AlertType
import scalafx.scene.control.Alert
import scalafx.Includes.*

import scala.collection.mutable.ListBuffer
import scala.util.matching.Regex

@FXML
class SignupPageController:

  @FXML private var firstNameField: TextField = _

  @FXML private var lastNameField: TextField = _

  @FXML private var emailField: TextField = _

  @FXML private var passwordField: PasswordField = _

  @FXML
  def handleBackToSignIn(event: ActionEvent): Unit = {
    MainApp.loadLoginPage()
  }

  @FXML
  def handleCreateAccount(event: ActionEvent): Unit =
    if isInputValid then
      val firstName = firstNameField.text.value.trim
      val lastName = lastNameField.text.value.trim
      val email = emailField.text.value.trim
      val password = passwordField.text.value.trim

      Auth.register(firstName, lastName, email, password) match
        case Right(user) =>
          new Alert(AlertType.Information):
            title = "Registration Success"
            headerText = s"Welcome! ${user.firstName.value}"
            contentText = s"Click 'OK' to return to login page."
          .showAndWait()
          MainApp.loadLoginPage()

        case Left(error) =>
          new Alert(AlertType.Error):
            title = "Signup Failed"
            headerText = error
            contentText = "Please try again using a different email."
          .showAndWait()

  private def isInputValid: Boolean =
    val errorMessage   = ListBuffer[String]()

    val firstName = firstNameField.text.value.trim
    val lastName = lastNameField.text.value.trim
    val email    = emailField.text.value.trim
    val password = passwordField.text.value.trim

    val emailPattern: Regex = "^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,}$".r

    if (firstName == null || firstName.isEmpty) then
      errorMessage += "First name cannot be empty."
    if (lastName == null || lastName.isEmpty) then
      errorMessage += "Last name cannot be empty."
    if (email == null || email.isEmpty || !emailPattern.matches(email)) then
      errorMessage += "Please enter a valid email."
    if (password == null || password.isEmpty || password.length < 8) then
      errorMessage += "Password must be at least 8 characters."

    if errorMessage.isEmpty then
      true
    else
      new Alert(AlertType.Error):
        title = "Signup Failed"
        headerText = "Invalid Input"
        contentText = errorMessage.mkString("\n")
      .showAndWait()
      false