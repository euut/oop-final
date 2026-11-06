package com.sunway.foodordersystem.controller

import com.sunway.foodordersystem.MainApp
import com.sunway.foodordersystem.model.{Cart, Menu, MenuItem}
import javafx.event.ActionEvent
import javafx.fxml.{FXML, FXMLLoader}
import javafx.scene.Node
import javafx.scene.control.Label
import javafx.scene.layout.VBox
import scalafx.Includes.*
import scalafx.scene.control.{Alert, ButtonType}
import scalafx.scene.control.Alert.AlertType
import scalafx.scene.control.ButtonBar.ButtonData

@FXML
class MenuPageController:

  @FXML private var foodList: VBox = _

  @FXML private var drinkList: VBox = _

  @FXML private var allList: VBox = _

  @FXML
  def initialize(): Unit =
    val menuItems = Menu.items.sortBy(_.name.value)

    menuItems.foreach { item =>
      item.category.value match
        case "Food"  => foodList.children.add(loadMenuItemNode(item))
        case "Drink" => drinkList.children.add(loadMenuItemNode(item))
        case _       => // Ignore unknown categories

      allList.children.add(loadMenuItemNode(item))
    }
    
  @FXML
  def handleViewCart(event: ActionEvent): Unit =
    MainApp.loadCartPage()

  @FXML
  def handleLogout(event: ActionEvent): Unit = {
    val alert = new Alert(AlertType.Confirmation):
      initOwner(MainApp.stage)
      title = "Logout Confirmation"
      headerText = "Are you sure you want to logout?"
      contentText = "Your current session will be ended."
      buttonTypes = Seq(
        new ButtonType("Yes", ButtonData.Yes),
        new ButtonType("No", ButtonData.No)
      )

    val result = alert.showAndWait()

    result match {
      case Some(button) if button.buttonData == ButtonData.Yes =>
        val successAlert = new Alert(AlertType.Information):
          initOwner(MainApp.stage)
          title = "Logout Successful"
          headerText = "You have been logged out."
          contentText = "Thank you for using our services."
        .showAndWait()

        MainApp.logoutUser()
        // Clear the cart
        Cart.clear()
        // Redirect user back to welcome
        MainApp.loadWelcomePage()
      case _ =>
    }
  }

  private def loadMenuItemNode(item: MenuItem): Node =
    try
      val loader = new FXMLLoader(getClass.getResource("/com/sunway/foodordersystem/view/MenuItem.fxml"))
      val node = loader.load[Node]()
      val controller = loader.getController[MenuItemController]()
      controller.item = item
      node
    catch
      case e: Exception =>
        e.printStackTrace()
        new Label(s"Failed to load: ${item.name.value}") // fallback node