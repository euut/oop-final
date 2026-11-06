package com.sunway.foodordersystem.controller

import com.sunway.foodordersystem.MainApp
import javafx.event.ActionEvent
import javafx.fxml.FXML
import javafx.scene.control.PasswordField
import scalafx.stage.Stage
import scalafx.Includes.*
import scalafx.scene.control.Alert
import scalafx.scene.control.Alert.AlertType

@FXML
class AdminPasswordDialogController:

  @FXML private var passwordField: PasswordField = _

  var dialogStage: Option[Stage] = None
  var okClicked: Boolean = false
  val password: String = "1234"

  @FXML
  def handleOK(event: ActionEvent): Unit =
    if passwordField.text.value.trim == password then
      MainApp.loadAdminMenuEditPage()
      okClicked = true
      dialogStage.foreach(_.close())
    else
      new Alert(AlertType.Error):
        title = "Access Denied"
        headerText = "Incorrect Password"
        contentText = "Please try again."
      .showAndWait()

  @FXML
  def handleCancel(event: ActionEvent): Unit =
    dialogStage.foreach(_.close())