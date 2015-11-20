package models

import play.api.db.slick.Config.driver.simple._
import scala.slick.lifted.Tag
import play.api.Play.current
import play.api.db.slick.DB

case class Projet (id:Option[Long],titre:String,url : Option[String],imagePath : Option[String],description : String)
class Projets(tag: Tag) extends Table[Projet](tag, "PROJET") {
  def id = column[Long]("id", O.PrimaryKey, O.AutoInc)
  def titre = column[String]("titre", O.NotNull)
  def url = column[Option[String]]("url", O.Nullable)
  def imagePath = column[Option[String]]("imagePath",O.Nullable)
  def description = column[String]("description",O.Nullable)

  def * = (id.?, titre,url,imagePath,description) <>(Projet.tupled, Projet.unapply _)
}

object Projets extends DAO {

  def findById(id: Long)(implicit s: Session): Option[Projet] =
    Projets.where(_.id === id).firstOption

  def count(implicit s: Session): Int =
    Query(Projets.length).first

  def count(filter: String)(implicit s: Session): Int =
    Query(Projets.where(_.titre.toLowerCase like filter.toLowerCase).length).first

  def list = {
    DB.withSession{
      implicit session:Session =>
        val query = (for {
          projet <- Projets
        } yield (projet)).sortBy(_.titre)
        query.list.map(row => (row))
    }
  }

  def insert(projet: Projet)={
    DB.withSession{
      implicit session :Session =>
        (Projets returning Projets.map(_.id)) += projet
    }
  }

  def update(id: Long, projet : Projet)= {
    DB.withSession{
      implicit session :Session =>
    val projetAMettreAJour: Projet = projet.copy(Some(id))
    Projets.where(_.id === id).update(projetAMettreAJour)
    }
  }

  def delete(id: Long)={
    DB.withSession{
      implicit session :Session =>
        val photoPath =Projets.where(_.id === id).firstOption.get.imagePath
        Projets.where(_.id === id).delete
        if(photoPath.isDefined){
          import java.io.File
          val file = new File("public/images/projets/"+photoPath.get)
          file.delete
        }
    }
  }
}