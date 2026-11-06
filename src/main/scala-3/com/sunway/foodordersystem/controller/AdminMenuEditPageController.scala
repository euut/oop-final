package com.sunway.foodordersystem.controller

import com.sunway.foodordersystem.MainApp
import com.sunway.foodordersystem.model.{Drink, Food, Menu, MenuItem}
import javafx.event.ActionEvent
import javafx.fxml.FXML
import javafx.scene.control.{Label, TableColumn, TableView}
import scalafx.Includes.*
import scalafx.scene.control.Alert
import scalafx.scene.control.Alert.AlertType
import scalafx.scene.control.ButtonBar.ButtonData

@FXML
class AdminMenuEditPageController:

  @FXML private var menuItemTable: TableView[MenuItem] = _

  @FXML private var itemNameColumn: TableColumn[MenuItem, String] = _

  @FXML private var itemNameLabel: Label = _

  @FXML private var priceLabel: Label = _

  @FXML private var categoryLabel: Label = _

  @FXML private var imagePathLabel: Label = _

  @FXML private var caloriesLabel: Label = _

  @FXML private var sugarLabel: Label = _

  def initialize(): Unit =
    // initialize Table View display contents model
    menuItemTable.items = Menu.items
    // initialize columns's cell values
    itemNameColumn.cellValueFactory = { _.value.name }

    updateItemDetails(None)

    menuItemTable.selectionModel().selectedItem.onChange(
      (_, _, newVal) => updateItemDetails(Option(newVal))
    )

  private def updateItemDetails(item: Option[MenuItem]): Unit =
    item match
      case Some(menuItem) =>
        itemNameLabel.text  <== menuItem.name
        priceLabel.text     <== menuItem.price.asString("RM %.2f")
        imagePathLabel.text <== menuItem.imagePath
        categoryLabel.text  <== menuItem.category

        // Type-specific fields
        menuItem match
          case f: Food =>
            caloriesLabel.text = s"${f.calories.value} cal"
            sugarLabel.text = "–"
          case d: Drink =>
            sugarLabel.text = s"${d.sugarContent.value} g"
            caloriesLabel.text = "–"
          case _ =>
            caloriesLabel.text = "–"
            sugarLabel.text = "–"

      case None =>
        // Unbind all text properties first
        itemNameLabel.text.unbind()
        priceLabel.text.unbind()
        categoryLabel.text.unbind()
        imagePathLabel.text.unbind()

        itemNameLabel.text = ""
        priceLabel.text = ""
        categoryLabel.text = ""
        imagePathLabel.text = ""
        caloriesLabel.text = ""
        sugarLabel.text = ""

  @FXML
  def handleNewFood(event: ActionEvent): Unit =
    val newFood = new Food("")
    val okClicked = MainApp.loadAdminMenuEditDialog(newFood)
    if okClicked then Menu.add(newFood)

  @FXML
  def handleNewDrink(event: ActionEvent): Unit =
    val newDrink = new Drink("")
    val okClicked = MainApp.loadAdminMenuEditDialog(newDrink)
    if okClicked then Menu.add(newDrink)

  @FXML
  def handleEditItem(event: ActionEvent): Unit =
    val selectedItem = menuItemTable.selectionModel().selectedItem.value
    if selectedItem != null then
      val okClicked = MainApp.loadAdminMenuEditDialog(selectedItem)
      if okClicked then
        updateItemDetails(Some(selectedItem))
        selectedItem.save()
    else
      val alert = new Alert(AlertType.Warning):
        initOwner(MainApp.stage)
        title = "No Selection"
        headerText = "No Menu Item Selected"
        contentText = "Please select an item in the table."
      .showAndWait()

  @FXML
  def handleDeleteItem(event: ActionEvent): Unit =
    val selectedIndex = menuItemTable.selectionModel().selectedIndex.value
    if selectedIndex >= 0 then
      val alert = new Alert(AlertType.Confirmation):
        initOwner(MainApp.stage)
        title = "Delete Confirmation"
        headerText = "Are you sure you want to delete this item?"
        contentText = "This process cannot be undone."

      val result = alert.showAndWait()
      result match {
        case Some(button) if button.buttonData == ButtonData.OKDone =>
          val selectedItem = menuItemTable.items().remove(selectedIndex)
          selectedItem.delete()
        case _ =>
      }
    else
      val alert = new Alert(Alert.AlertType.Warning):
        initOwner(MainApp.stage)
        title = "No selection"
        headerText = "No item selected"
        contentText = "Please select an item in the table"
      alert.showAndWait()

  @FXML
  def handleSaveAndQuit(event: ActionEvent): Unit =
    MainApp.loadWelcomePage()