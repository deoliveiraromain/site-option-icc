package models

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global
import play.api.db.slick.Config.driver.simple._
import scala.slick.lifted.Tag
import play.api.Play.current
import play.api.db.slick.DB
import javax.management.remote.rmi._RMIConnection_Stub
import scala.slick.jdbc.meta.MBestRowIdentifierColumn.Scope.Session

case class Account(id: Option[Long], email: String, password: String)//, permission: Permission)
class Accounts(tag: Tag) extends Table[Account](tag, "ACCOUNT") {

  def id = column[Long]("id", O.PrimaryKey, O.AutoInc)
  def email = column[String]("email", O.NotNull)
  def password = column[String]("password", O.NotNull)

  def * = (id.?, email, password) <>(Account.tupled, Account.unapply _)
}

object Accounts extends DAO {

  def findById(id: Long): Option[Account] = {
    DB.withSession{
      implicit session:Session =>
      Accounts.where(_.id === id).firstOption
    }
  }

  def count: Int = {
    DB.withSession{
      implicit session:Session =>
        Query(Accounts.length).first
    }

  }

  def list = {
    DB.withSession{
      implicit session:Session =>
        val query = (for {
          account <- Accounts
        } yield (account)).sortBy(_.email)
        query.list.map(row => (row))
    }
  }

  def insert(account: Account) {
    DB.withSession{
      implicit session:Session =>
      Accounts.insert(account)
    }
  }

  def update(id: Long, account: Account) {
    DB.withSession{
      implicit session:Session =>
      val accountAMettreAjour: Account = account.copy(Some(id))
      Accounts.where(_.id === id).update(accountAMettreAjour)
    }
  }

  def delete(id: Long) {
    DB.withSession{
      implicit session:Session =>
      Accounts.where(_.id === id).delete
    }
  }
  def authenticate(email: String, password: String) : Future[Option[Account]] = {
    DB.withSession{
      implicit session:Session =>
          val query = for{
        account <- Accounts if (account.email === email && account.password === password)
      }yield(account)
      Future(query.firstOption)
    }
  }

  def findByEmail(email: String) :Future[Option[Account]] = {
    DB.withSession{
      implicit session:Session =>
      Future(Accounts.where(_.email ===  email).firstOption)
    }
  }

  def findAll: Future[List[Account]] = {
    DB.withSession{
      implicit session:Session =>
      Future(Accounts.list)
    }
  }
}
