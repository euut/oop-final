package com.sunway.foodordersystem.controller

import com.sunway.foodordersystem.MainApp
import javafx.event.ActionEvent
import javafx.fxml.FXML

@FXML
class RootLayoutController:

  @FXML
  def handleClose(event: ActionEvent): Unit =
    System.exit(0)

  @FXML
  def handleGoToMenu(event: ActionEvent): Unit =
    if MainApp.isLoggedIn then
      MainApp.loadMenuPage()
    else
      MainApp.loadLoginPage()

  @FXML
  def handleGoToCart(event: ActionEvent): Unit =
    if MainApp.isLoggedIn then
      MainApp.loadCartPage()
    else
      MainApp.loadLoginPage()

  @FXML
  def handleAdminEditMenu(event: ActionEvent): Unit =
    MainApp.loadAdminPasswordDialog()

  @FXML
  def handleAbout(event: ActionEvent): Unit =
    MainApp.loadAboutDialog()