package models

import play.api.db.slick.Config.driver.simple._
import scala.slick.lifted.Tag

private[models] trait DAO {
  val Eleves = TableQuery[Eleves]
  val Projets = TableQuery[Projets]
  val Accounts = TableQuery[Accounts]
  val Professeurs = TableQuery[Professeurs]
}