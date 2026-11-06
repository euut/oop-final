package com.sunway.foodordersystem.model

import scalafx.beans.property.ObjectProperty

class Drink(nameS: String) extends MenuItem(nameS):
  def this() = this(null)

  category.value = "Drink"

  val sugarContent: ObjectProperty[Int] = ObjectProperty[Int](0)

  override def description: String = s"Sugar: ${sugarContent.value} g"

object Drink:
  def apply(
             _id: Int,
             name: String,
             price: Double,
             imagePath: String,
             sugar: Int
           ): Drink =
    val d = new Drink(name)
    d.id.value = _id
    d.price.value = price
    d.imagePath.value = imagePath
    d.sugarContent.value = sugar
    d
