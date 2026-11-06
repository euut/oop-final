package com.sunway.foodordersystem.model

import scalafx.collections.ObservableBuffer

object Menu:
  private val menuItems: ObservableBuffer[MenuItem] = ObservableBuffer()

  def items: ObservableBuffer[MenuItem] = menuItems

  def load(): Unit =
    menuItems ++= MenuItem.getAll()

  def add(item: MenuItem): Unit = {
    menuItems += item
    val id = item.save()
    id.map(x => item.id.value = x)
  }

  def remove(item: MenuItem): Unit =
    menuItems.remove(item)
