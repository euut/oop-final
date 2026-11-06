package com.sunway.foodordersystem.model

import scalafx.beans.property.ObjectProperty

class Food(nameS: String) extends MenuItem(nameS):
  def this() = this(null)

  category.value = "Food"

  val calories: ObjectProperty[Int] = ObjectProperty[Int](0)

  override def description: String = s"Calories: ${calories.value} cal"

object Food:
  def apply(
             _id: Int,
             name: String,
             price: Double,
             imagePath: String,
             calories: Int
           ): Food =
    val f = new Food(name)
    f.id.value = _id
    f.price.value = price
    f.imagePath.value = imagePath
    f.calories.value = calories
    f
