package com.sunway.foodordersystem.model

import com.sunway.foodordersystem.util.Database
import scalafx.beans.property.{ObjectProperty, StringProperty}
import scalikejdbc.*

import scala.util.Try

abstract class MenuItem(nameS: String) extends Database:
  def this() = this(null)

  val id        = ObjectProperty[Int](-1)
  val name      = StringProperty(nameS)
  val price     = ObjectProperty[Double](0.0)
  val category  = StringProperty("Food")
  val imagePath = StringProperty("img.png")

  def description: String

  def save(): Try[Int] =
    val calories = this match
      case food: Food => Some(food.calories.value)
      case _          => None

    val sugar = this match
      case drink: Drink => Some(drink.sugarContent.value)
      case _            => None

    if (!isExist) then
      Try(DB autoCommit { implicit session =>
        sql"""
        insert into menu_items (name, price, category, imagePath, calories, sugar)
          values (
            ${name.value},
            ${price.value},
            ${category.value},
            ${imagePath.value},
            $calories,
            $sugar
          )
        """.updateAndReturnGeneratedKey.apply().toInt
      })
    else
      Try(DB autoCommit { implicit session =>
        sql"""
          update menu_items set
            name = ${name.value},
            price = ${price.value},
            category = ${category.value},
            imagePath = ${imagePath.value},
            calories = $calories,
            sugar = $sugar
          where id = ${id.value}
        """.update.apply()
      })

  def delete(): Try[Int] =
    if isExist then
      Try(DB autoCommit { implicit session =>
        sql"delete from menu_items where id = ${id.value}"
          .update
          .apply()
      })
    else
      throw new Exception("Menu item does not exist in database")

  def isExist: Boolean =
    DB readOnly { implicit session =>
      sql"select * from menu_items where id = ${id.value}"
        .map(rs => rs.string("name")).single.apply()
    } match
      case Some(x) => true
      case None => false

object MenuItem extends Database:

  def initializeTable(): Unit =
    DB autoCommit { implicit session =>
      sql"""
        create table menu_items (
          id int not null GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1),
          name varchar(100),
          price double,
          category varchar(20),
          imagePath varchar(255),
          calories int,
          sugar int
        )
      """.execute.apply()
    }

  def getAll(): List[MenuItem] =
    DB readOnly { implicit session =>
      sql"select * from menu_items".map { rs =>
        rs.string("category") match
          case "Food" =>
            Food(
              rs.int("id"),
              rs.string("name"),
              rs.double("price"),
              rs.string("imagePath"),
              rs.int("calories")
            )
          case "Drink" =>
            Drink(
              rs.int("id"),
              rs.string("name"),
              rs.double("price"),
              rs.string("imagePath"),
              rs.int("sugar")
            )
          case other =>
            throw new Exception(s"Unknown category $other")
      }.list.apply()
    }
