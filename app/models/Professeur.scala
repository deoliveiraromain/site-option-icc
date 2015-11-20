package models

import play.api.db.slick.Config.driver.simple._
import scala.slick.lifted.Tag
import play.api.Play.current
import play.api.db.slick.DB

case class Professeur(id: Option[Long], prenom: String,nom : String,email : Option[String],telephone : Option[String],photo : Option[String],description : Option[String],departement : Option[String])
class Professeurs(tag: Tag) extends Table[Professeur](tag, "PROFESSEUR") {

  def id = column[Long]("id", O.PrimaryKey, O.AutoInc)
  def nom = column[String]("nom", O.NotNull)
  def prenom = column[String]("prenom", O.NotNull)
  def email = column[Option[String]]("email", O.Nullable)
  def telephone = column[Option[String]]("telephone", O.Nullable)
  def photo = column[Option[String]]("photo",O.Nullable)
  def description = column[Option[String]]("descrition",O.Nullable)
  def departement = column[Option[String]]("departement",O.Nullable)

  def * = (id.?, nom,prenom,email,telephone,photo,description,departement) <>(Professeur.tupled, Professeur.unapply _)
}

object Professeurs extends DAO {

  def findById(id: Long)(implicit s: Session): Option[Professeur] =
    Professeurs.where(_.id === id).firstOption

  def count(implicit s: Session): Int =
    Query(Professeurs.length).first

  def list = {
    DB.withSession{
      implicit session:Session =>
        val query = (for {
          professeur <- Professeurs
        } yield (professeur)).sortBy(_.nom)
        query.list.map(row => (row))
    }
  }

  def insert(professeur: Professeur)= {
    DB.withSession{
      implicit session :Session =>
        (Professeurs returning Professeurs.map(_.id)) += professeur
    }
  }

  def update(id: Long, professeur : Professeur)= {
    DB.withSession{
      implicit session : Session=>
        val professeurAMettreAjour: Professeur = professeur.copy(Some(id))
        Professeurs.where(_.id === id).update(professeurAMettreAjour)
    }
  }


  def delete(id: Long)= {
    DB.withSession{
      implicit session :Session =>
        val photoPath =Professeurs.where(_.id === id).firstOption.get.photo
        Professeurs.where(_.id === id).delete
        if(photoPath.isDefined){
          import java.io.File
          val file = new File("public/images/profs/"+photoPath.get)
          file.delete
        }
    }
  }
}