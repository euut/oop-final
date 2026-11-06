package com.sunway.foodordersystem.controller

import com.sunway.foodordersystem.MainApp
import com.sunway.foodordersystem.model.{Cart, CartItem}
import javafx.event.ActionEvent
import javafx.fxml.FXML
import javafx.scene.control.{Label, TableColumn, TableView}
import scalafx.Includes.*
import scalafx.beans.binding.Bindings
import scalafx.scene.control.Alert.AlertType
import scalafx.scene.control.ButtonBar.ButtonData
import scalafx.scene.control.{Alert, ButtonType}

@FXML
class CartPageController:

  @FXML private var cartTable: TableView[CartItem] = _

  @FXML private var nameColumn: TableColumn[CartItem, String] = _

  @FXML private var priceColumn: TableColumn[CartItem, Double] = _

  @FXML private var quantityColumn: TableColumn[CartItem, Int] = _

  @FXML private var subtotalColumn: TableColumn[CartItem, Double] = _

  @FXML private var totalLabel: Label = _

  @FXML
  def initialize(): Unit = {
    cartTable.items = Cart.items

    nameColumn.cellValueFactory = { _.value.name }
    priceColumn.cellValueFactory = { _.value.price }
    quantityColumn.cellValueFactory = { _.value.quantity }
    subtotalColumn.cellValueFactory = { _.value.subtotal }

    updateTotal()
  }

  @FXML
  def handleBackToMenu(event: ActionEvent): Unit =
    MainApp.loadMenuPage()

  @FXML
  def handleDelete(event: ActionEvent): Unit =
    if Cart.items.nonEmpty then
      val selectedIndex = cartTable.selectionModel().selectedIndex.value

      if selectedIndex >= 0 then
        val alert = new Alert(AlertType.Confirmation):
          initOwner(MainApp.stage)
          title = "Delete Confirmation"
          headerText = "Are you sure you want to delete this item from the cart?"
          contentText = "This process cannot be undone."

        val result = alert.showAndWait()
        result match {
          case Some(button) if button.buttonData == ButtonData.OKDone =>
            // Remove item
            Cart.items.remove(selectedIndex)
            updateTotal()
          case _ =>
        }
      else
        val alert = new Alert(AlertType.Information):
          initOwner(MainApp.stage)
          title = "Failed to Delete"
          headerText = "No item selected"
          contentText = "Please select an item in the cart."
        .showAndWait()

  @FXML
  def handleIncrement(event: ActionEvent): Unit =
    if Cart.items.nonEmpty then
      val selectedIndex = cartTable.selectionModel().selectedIndex.value

      if selectedIndex >= 0 then
        Cart.items.get(selectedIndex).quantity() += 1
        updateTotal()
      else
        new Alert(AlertType.Information):
          initOwner(MainApp.stage)
          title = "No Selection"
          headerText = "No item selected"
          contentText = "Please select an item in the cart."
        .showAndWait()

  @FXML
  def handleDecrement(event: ActionEvent): Unit =
    if Cart.items.nonEmpty then
      val selectedIndex = cartTable.selectionModel().selectedIndex.value

      if selectedIndex >= 0 then
        val item = Cart.items.get(selectedIndex)
        if item.quantity() > 1 then
          item.quantity() -= 1
          updateTotal()
        else
          handleDelete(event)
      else
        new Alert(AlertType.Information):
          initOwner(MainApp.stage)
          title = "No Selection"
          headerText = "No item selected"
          contentText = "Please select an item in the cart."
        .showAndWait()

  @FXML
  def handleClearCart(event: ActionEvent): Unit =
    if Cart.items.nonEmpty then
      val alert = new Alert(AlertType.Confirmation):
        initOwner(MainApp.stage)
        title = "Clear Cart Confirmation"
        headerText = "Are you sure you want to clear the cart?"
        contentText = "This process cannot be undone."

      val result = alert.showAndWait()
      result match {
        case Some(button) if button.buttonData == ButtonData.OKDone =>
          Cart.clear()
          updateTotal()
        case _ =>
      }

  @FXML
  def handleCheckout(event: ActionEvent): Unit =
    if Cart.items.nonEmpty then

      val alert = new Alert(AlertType.Confirmation):
        initOwner(MainApp.stage)
        title = "Checkout Confirmation"
        headerText = "Are you sure you want to checkout?"
        buttonTypes = Seq(
          new ButtonType("Yes", ButtonData.Yes),
          new ButtonType("No", ButtonData.No)
        )

      val result = alert.showAndWait()
      result match {
        case Some(button) if button.buttonData == ButtonData.Yes =>
          val successAlert = new Alert(AlertType.Information):
            initOwner(MainApp.stage)
            title = "Checkout Successful"
            headerText = "Your order has been placed!"
          .showAndWait()

          // Redirect user back to menu page after checkout
          MainApp.loadMenuPage()
          // Clear cart
          Cart.clear()
          updateTotal()

        case _ =>
      }

    else
      val alert = new Alert(AlertType.Warning):
        initOwner(MainApp.stage)
        title = "Checkout Unsuccessful"
        headerText = "Your cart is currently empty"
        contentText = "Please add at least one item."
      .showAndWait()

  private def updateTotal(): Unit =
    totalLabel.text <== Bindings.createStringBinding(
      () => f"RM ${Cart.total}%.2f",
      Cart.items
    )