package com.sunway.foodordersystem.controller

import com.sunway.foodordersystem.model.{Drink, Food, MenuItem}
import javafx.event.ActionEvent
import javafx.fxml.FXML
import javafx.scene.control.{Label, TextField}
import scalafx.Includes.*
import scalafx.scene.control.Alert
import scalafx.scene.control.Alert.AlertType
import scalafx.stage.Stage

import scala.collection.mutable.ListBuffer

@FXML
class AdminMenuEditDialogController:

  @FXML private var itemNameField: TextField = _

  @FXML private var priceField: TextField = _

  @FXML private var categoryLabel: Label = _

  @FXML private var imagePathField: TextField = _

  @FXML private var caloriesField: TextField = _

  @FXML private var sugarField: TextField = _

  var dialogStage: Option[Stage] = None
  private var __item: MenuItem = null
  var okClicked = false

  def item: MenuItem = __item

  def item_=(x: MenuItem): Unit =
    __item = x

    itemNameField.text.value = x.name.value
    priceField.text.value = x.price.value.toString
    categoryLabel.text.value = x.category.value
    imagePathField.text.value = x.imagePath.value

    x match
      case f: Food =>
        caloriesField.text.value = f.calories.value.toString
        caloriesField.setDisable(false)
        sugarField.text.value = ""
        sugarField.setDisable(true)

      case d: Drink =>
        sugarField.text.value = d.sugarContent.value.toString
        sugarField.setDisable(false)
        caloriesField.text.value = ""
        caloriesField.setDisable(true)

      case _ =>

  def handleOk(action: ActionEvent): Unit =
    if isInputValid then
      __item.name.value  = itemNameField.text.value
      __item.price.value = priceField.getText.toDouble
      __item.imagePath.value = imagePathField.text.value

      __item match
        case f: Food =>
          f.calories.value = caloriesField.getText.toInt
        case d: Drink =>
          d.sugarContent.value = sugarField.getText.toInt
        case _ =>

      okClicked = true
      dialogStage.foreach(_.close())

  def handleCancel(action: ActionEvent): Unit =
    dialogStage.foreach(_.close())

  private def nullOrEmpty(s: String): Boolean = s == null || s.trim.isEmpty

  private def isInputValid: Boolean =
    val errorMessage = ListBuffer[String]()

    val itemName = itemNameField.text.value
    val price = priceField.text.value
    val imagePath = imagePathField.text.value

    if nullOrEmpty(itemName) then
      errorMessage += "Please enter a valid name."
    if nullOrEmpty(price) then
      errorMessage += "Please enter a valid price."
    else
      try price.toDouble
      catch case _: NumberFormatException => errorMessage += "Price must be a number."
    if nullOrEmpty(imagePath) then
      errorMessage += "Please enter a valid image path (eg. image_name.png)."

    __item match
      case f: Food =>
        val calories = caloriesField.text.value
        if nullOrEmpty(calories) then
          errorMessage += "Please enter a valid calories."
        else
          try calories.toInt
          catch case _: NumberFormatException => errorMessage += "Calories must be an integer."

      case d: Drink =>
        val sugar = sugarField.text.value
        if nullOrEmpty(sugar) then
          errorMessage += "No valid sugar!"
        else
          try sugar.toInt
          catch case _: NumberFormatException => errorMessage += "Sugar must be an integer!"

      case _ => // Do nothing

    if errorMessage.isEmpty then
      true
    else
      val alert = new Alert(AlertType.Error):
        initOwner(dialogStage.getOrElse(null))
        title = "Invalid Fields"
        headerText = "Please correct invalid fields"
        contentText = errorMessage.mkString("\n")
      .showAndWait()
      false