package com.sunway.foodordersystem.model

import scalafx.collections.ObservableBuffer

object Cart:
  private val cartItems: ObservableBuffer[CartItem] = ObservableBuffer()
  
  def items: ObservableBuffer[CartItem] = cartItems

  def addItem(menuItem: MenuItem, quantity: Int): Unit =
    cartItems.find(_.menuItem == menuItem) match
      case Some(existing) => existing.quantity.value += quantity
      case None           => cartItems += new CartItem(menuItem, quantity)

  def clear(): Unit =
    cartItems.clear()

  def total: Double =
    cartItems.map(_.subtotal.value).sum