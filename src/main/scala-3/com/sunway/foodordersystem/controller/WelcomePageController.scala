package com.sunway.foodordersystem.controller

import com.sunway.foodordersystem.MainApp
import javafx.event.ActionEvent
import javafx.fxml.{FXML, FXMLLoader}
import javafx.scene.control.Label

@FXML
class WelcomePageController:

  @FXML
  def handleStart(event: ActionEvent): Unit = {
    MainApp.loadLoginPage()
  }