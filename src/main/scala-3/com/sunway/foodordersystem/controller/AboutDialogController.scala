package com.sunway.foodordersystem.controller

import javafx.event.ActionEvent
import javafx.fxml.FXML
import scalafx.stage.Stage

@FXML
class AboutDialogController:
  
  var dialogStage : Option[Stage] = None
  var okClicked   : Boolean = false

  @FXML
  def handleClose(event: ActionEvent): Unit =
    okClicked = true
    dialogStage.foreach(_.close())