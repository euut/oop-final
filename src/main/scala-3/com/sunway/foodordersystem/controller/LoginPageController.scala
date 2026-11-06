package com.sunway.foodordersystem.controller

import com.sunway.foodordersystem.MainApp
import com.sunway.foodordersystem.util.Auth
import javafx.event.ActionEvent
import javafx.fxml.FXML
import javafx.scene.control.{PasswordField, TextField}
import scalafx.scene.control.Alert
import scalafx.scene.control.Alert.AlertType
import scalafx.Includes.*

import scala.collection.mutable.ListBuffer
import scala.util.matching.Regex

@FXML
class LoginPageController:

  @FXML private var emailField: TextField = _

  @FXML private var passwordField: PasswordField = _

  @FXML
  def handleSignUp(event: ActionEvent): Unit = {
    MainApp.loadSignupPage()
  }

  @FXML
  def handleSignIn(event: ActionEvent): Unit =
    if isInputValid then
      val email = emailField.text.value.trim
      val password = passwordField.text.value.trim

      Auth.login(email, password) match
        case Right(user) =>
          MainApp.login(user)
          MainApp.loadMenuPage()
        case Left(error) =>
          new Alert(AlertType.Error):
            title = "Login Failed"
            headerText = error
            contentText = "Please try again."
          .showAndWait()

  private def isInputValid: Boolean =
    val errorMessage = ListBuffer[String]()
    val email = emailField.text.value.trim
    val password = passwordField.text.value.trim

    val emailPattern: Regex = "^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,}$".r

    if email == null || email.isEmpty || !emailPattern.matches(email) then
      errorMessage += "Please enter a valid email."
    if password == null || password.isEmpty || password.length < 8 then
      errorMessage += "Password must be at least 8 characters."

    if errorMessage.isEmpty then
      true
    else
      new Alert(AlertType.Error):
        title = "Login Failed"
        headerText = "Invalid Input"
        contentText = errorMessage.mkString("\n")
      .showAndWait()
      false