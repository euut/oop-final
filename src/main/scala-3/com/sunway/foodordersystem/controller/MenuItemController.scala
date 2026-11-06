package com.sunway.foodordersystem.controller

import com.sunway.foodordersystem.MainApp
import com.sunway.foodordersystem.model.MenuItem
import javafx.fxml.FXML
import javafx.scene.control.Label
import javafx.scene.image.{Image, ImageView}
import javafx.scene.input.MouseEvent
import scalafx.Includes.*

@FXML
class MenuItemController:

  @FXML private var itemImage: ImageView = _

  @FXML private var itemNameLabel: Label = _

  @FXML private var itemPriceLabel: Label = _

  private var __item: MenuItem = null

  def item: MenuItem = __item

  def item_=(x: MenuItem): Unit = {
    __item = x

    itemNameLabel.text.value = x.name.value
    itemPriceLabel.text.value = f"RM${x.price.value}%.2f"

    val resource = Option(getClass.getResource("/images/" + x.imagePath.value))
    resource match {
      case Some(url) =>
        itemImage.image = new Image(url.toExternalForm)
      case None =>
        println(s"[ERROR]: image not found at path '${x.imagePath.value}'")
    }
  }

  @FXML
  def handleClick(event: MouseEvent): Unit = {
    MainApp.loadMenuItemDialog(__item)
  }