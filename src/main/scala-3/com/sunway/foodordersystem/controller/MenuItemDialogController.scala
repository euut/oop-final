package com.sunway.foodordersystem.controller

import com.sunway.foodordersystem.model.{Cart, MenuItem}
import javafx.event.ActionEvent
import javafx.fxml.FXML
import javafx.scene.control.Label
import javafx.scene.image.{Image, ImageView}
import scalafx.Includes.*
import scalafx.beans.binding.Bindings
import scalafx.beans.property.ObjectProperty
import scalafx.stage.Stage

@FXML
class MenuItemDialogController:

  @FXML private var itemImage: ImageView = _

  @FXML private var itemNameLabel: Label = _

  @FXML private var itemPriceLabel: Label = _

  @FXML private var itemDescriptionLabel: Label = _

  @FXML private var quantityLabel: Label = _

  private var quantity = ObjectProperty[Int](1)

  private var __item : MenuItem = null
  var dialogStage    : Option[Stage] = None
  var okClicked      : Boolean = false

  def item: MenuItem = __item

  def item_=(x: MenuItem): Unit = {
    __item = x

    itemNameLabel.text.value = x.name.value
    itemPriceLabel.text.value = f"RM ${x.price.value}%.2f"
    itemDescriptionLabel.text.value = x.description

    Option(getClass.getResource("/images/" + x.imagePath.value)) match {
      case Some(url) =>
        itemImage.image = new Image(url.toExternalForm)
      case None =>
        println(s"[ERROR]: image '${x.imagePath.value}' not found")
    }
  }

  @FXML
  def initialize(): Unit =
    quantityLabel.text <== Bindings.createStringBinding(
      () => f"${quantity.value}",
      quantity
    )

  @FXML
  def handleIncrement(event: ActionEvent): Unit =
    quantity.value += 1

  @FXML
  def handleDecrement(event: ActionEvent): Unit =
    if quantity.value > 1 then
      quantity.value -= 1

  @FXML
  def handleCancel(event: ActionEvent): Unit =
    dialogStage.foreach(_.close())

  @FXML
  def handleAddToCart(event: ActionEvent): Unit =
    Cart.addItem(item, quantity.value)

    okClicked = true
    dialogStage.foreach(_.close())
