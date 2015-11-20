package models

import play.api.db.slick.Config.driver.simple._
import scala.slick.lifted.Tag
import play.api.Play.current
import play.api.db.slick.DB
import javax.management.remote.rmi._RMIConnection_Stub
import scala.slick.jdbc.meta.MBestRowIdentifierColumn.Scope.Session
import play.mvc.Results.Todo
import play.api.Logger

case class Eleve(id: Option[Long],promotion : Int, prenom: String,nom : String,photo : Option[String],pseudo : Option[String])
class Eleves(tag: Tag) extends Table[Eleve](tag, "ELEVE") {

  def id = column[Long]("id", O.PrimaryKey, O.AutoInc)
  def promotion = column[Int]("promotion", O.NotNull)
  def nom = column[String]("nom", O.NotNull)
  def prenom = column[String]("prenom", O.NotNull)
  def photo = column[Option[String]]("photo",O.Nullable)
  def pseudo = column[Option[String]]("pseudo",O.Nullable)

  def * = (id.?,promotion,prenom,nom,photo,pseudo) <>(Eleve.tupled, Eleve.unapply _)
}

object Eleves extends DAO {

  def findById(id: Long)(implicit s: Session): Option[Eleve] =
    Eleves.where(_.id === id).firstOption

  def count(implicit s: Session): Int =
    Query(Eleves.length).first

  def count(promotion: Int)(implicit s: Session): Int =
    Query(Eleves.where(_.promotion === promotion).length).first

  def list = {
    DB.withSession{
      implicit session:Session =>
      val query = (for {
        eleve <- Eleves
      } yield (eleve)).sortBy(_.nom).sortBy(_.promotion desc)
      query.list.map(row => (row))
    }
  }

  def listPromotions = {
    DB.withSession{
      implicit session:Session =>
        Eleves.groupBy(_.promotion).map(_._1).sorted.run.reverse
    }
  }

  def insert(eleve: Eleve):Long = {
    DB.withSession{
    implicit session :Session =>
      (Eleves returning Eleves.map(_.id)) += eleve
    }
  }

  def update(id: Long, eleve: Eleve)= {
    DB.withSession{
    implicit session : Session=>
    val eleveAMettreAjour: Eleve = eleve.copy(Some(id))
    Eleves.where(_.id === id).update(eleveAMettreAjour)
    }
  }

  def upsert(id : Option[Long], eleve : Eleve)={
    DB.withSession{
      implicit session : Session=>

    }
  }

  def delete(id: Long)= {
    DB.withSession{
      implicit session :Session =>
      val photoPath =Eleves.where(_.id === id).firstOption.get.photo
      Eleves.where(_.id === id).delete
      if(photoPath.isDefined){
        import java.io.File
        val file = new File("public/images/eleves/"+photoPath.get)
        file.delete
      }
    }
  }
}