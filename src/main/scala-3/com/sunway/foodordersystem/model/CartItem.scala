package com.sunway.foodordersystem.model

import scalafx.beans.binding.Bindings
import scalafx.beans.property.{ObjectProperty, StringProperty}

class CartItem(val menuItem: MenuItem, initialQty: Int):

  val name     = StringProperty(menuItem.name.value)
  val price    = ObjectProperty[Double](menuItem.price.value)
  val quantity = ObjectProperty[Int](initialQty)
  val subtotal = ObjectProperty[Double](0.0)
  
  subtotal <== Bindings.createObjectBinding(() => 
    price.value * quantity.value, price, quantity
  )