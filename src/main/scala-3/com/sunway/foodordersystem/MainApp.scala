package com.sunway.foodordersystem

import com.sunway.foodordersystem.controller.{AboutDialogController, AdminMenuEditDialogController, AdminPasswordDialogController, MenuItemDialogController}
import com.sunway.foodordersystem.model.{Menu, MenuItem, User}
import com.sunway.foodordersystem.util.Database
import javafx.fxml.FXMLLoader
import scalafx.Includes.*
import scalafx.application.JFXApp3
import scalafx.application.JFXApp3.PrimaryStage
import scalafx.scene.Scene
import scalafx.scene.image.Image
import scalafx.stage.{Modality, Stage}

object MainApp extends JFXApp3:
  Database.setupDB()

  private var currentUser: Option[User] = None

  private var roots: Option[scalafx.scene.layout.BorderPane] = None
  private val cssResource = getClass.getResource("/com/sunway/foodordersystem/view/css/Style.css")

  // Load the menu
  Menu.load()

  override def start(): Unit = {

    val rootResource = getClass.getResource("/com/sunway/foodordersystem/view/RootLayout.fxml")

    val loader = new FXMLLoader(rootResource)
    loader.load()

    roots = Option(loader.getRoot[javafx.scene.layout.BorderPane])

    stage = new PrimaryStage():
      title = "Food Ordering System"
      icons += new Image(getClass.getResource("/images/icon.png").toExternalForm)
      scene = new Scene():
        root = roots.get
        stylesheets = Seq(cssResource.toExternalForm)

    loadWelcomePage()
  }

  // Keep track current logged-in user
  def login(user: User): Unit =
    currentUser = Some(user)

  def logoutUser(): Unit =
    currentUser = None

  def isLoggedIn: Boolean = currentUser.isDefined

  // FXML loading logic
  def loadWelcomePage(): Unit =
    val resource = getClass.getResource("/com/sunway/foodordersystem/view/WelcomePage.fxml")
    val loader = new FXMLLoader(resource)
    val root = loader.load[javafx.scene.Parent]()
    roots.foreach(_.setCenter(root))

  def loadLoginPage(): Unit =
    val resource = getClass.getResource("/com/sunway/foodordersystem/view/LoginPage.fxml")
    val loader = new FXMLLoader(resource)
    val root = loader.load[javafx.scene.Parent]()
    roots.foreach(_.setCenter(root))

  def loadSignupPage(): Unit =
    val resource = getClass.getResource("/com/sunway/foodordersystem/view/SignupPage.fxml")
    val loader = new FXMLLoader(resource)
    val root = loader.load[javafx.scene.Parent]()
    roots.foreach(_.setCenter(root))
  
  def loadMenuPage(): Unit =
    val resource = getClass.getResource("/com/sunway/foodordersystem/view/MenuPage.fxml")
    val loader = new FXMLLoader(resource)
    val root = loader.load[javafx.scene.Parent]()
    roots.foreach(_.setCenter(root))

  def loadCartPage(): Unit =
    val resource = getClass.getResource("/com/sunway/foodordersystem/view/CartPage.fxml")
    val loader = new FXMLLoader(resource)
    val root = loader.load[javafx.scene.Parent]()
    roots.foreach(_.setCenter(root))

  def loadAdminMenuEditPage(): Unit =
    val resource = getClass.getResource("/com/sunway/foodordersystem/view/AdminMenuEditPage.fxml")
    val loader = new FXMLLoader(resource)
    val root = loader.load[javafx.scene.Parent]()
    roots.foreach(_.setCenter(root))

  def loadAdminPasswordDialog(): Boolean =
    val resource = getClass.getResource("/com/sunway/foodordersystem/view/AdminPasswordDialog.fxml")
    val loader = new FXMLLoader(resource)
    val dialogRoot = loader.load[javafx.scene.Parent]()
    val controller = loader.getController[AdminPasswordDialogController]

    val dialog = new Stage():
      initModality(Modality.ApplicationModal)
      initOwner(stage)
      title = "Admin Access Required"
      resizable = false
      scene = new Scene():
        root = dialogRoot
        stylesheets = Seq(cssResource.toExternalForm)

    controller.dialogStage = Option(dialog)
    dialog.showAndWait()
    controller.okClicked

  def loadAdminMenuEditDialog(item: MenuItem): Boolean =
    val resource = getClass.getResource("/com/sunway/foodordersystem/view/AdminMenuEditDialog.fxml")
    val loader = new FXMLLoader(resource)
    val dialogRoot = loader.load[javafx.scene.Parent]()
    val controller = loader.getController[AdminMenuEditDialogController]

    val dialog = new Stage():
      initModality(Modality.ApplicationModal)
      initOwner(stage)
      title = item.name.value
      resizable = false
      scene = new Scene():
        root = dialogRoot
        stylesheets = Seq(cssResource.toExternalForm)

    controller.dialogStage = Option(dialog)
    controller.item = item
    dialog.showAndWait()
    controller.okClicked

  def loadAboutDialog(): Boolean =
    val resource = getClass.getResource("/com/sunway/foodordersystem/view/AboutDialog.fxml")
    val loader = new FXMLLoader(resource)
    val dialogRoot = loader.load[javafx.scene.Parent]()
    val controller = loader.getController[AboutDialogController]

    val dialog = new Stage():
      initModality(Modality.ApplicationModal)
      initOwner(stage)
      title = "About"
      resizable = false
      scene = new Scene():
        root = dialogRoot
        stylesheets = Seq(cssResource.toExternalForm)

    controller.dialogStage = Option(dialog)
    dialog.showAndWait()
    controller.okClicked

  def loadMenuItemDialog(item: MenuItem): Boolean =
    val resource = getClass.getResource("/com/sunway/foodordersystem/view/MenuItemDialog.fxml")
    val loader = new FXMLLoader(resource)
    val dialogRoot = loader.load[javafx.scene.Parent]()
    val controller = loader.getController[MenuItemDialogController]

    val dialog = new Stage():
      initModality(Modality.ApplicationModal)
      initOwner(stage)
      title = item.name.value
      resizable = false
      scene = new Scene():
        root = dialogRoot
        stylesheets = Seq(cssResource.toExternalForm)

    controller.dialogStage = Option(dialog)
    controller.item = item
    dialog.showAndWait()
    controller.okClicked
