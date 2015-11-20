

import play.api._
import models._
import play.api.db.slick._
import play.api.mvc.{SimpleResult, RequestHeader}
import play.api.Play.current




import play.api.mvc.Results._
import scala.concurrent.Future


object Global extends GlobalSettings {

  override def onStart(app: Application) {
    InitialData.insert()
  }

  override def onBadRequest(request: RequestHeader, error: String) = {
    Future.successful(BadRequest("Bad Request: " + error))
  }

  override def onError(request: RequestHeader, ex: Throwable): Future[SimpleResult] = {
    Future.successful(NotFound("Error: " +ex.getMessage))
  }

  override def onHandlerNotFound(request: RequestHeader): Future[SimpleResult] = {
    Future.successful(NotFound(views.html.notfound()))
  }
}

/**
 * Initial set of data to be imported
 * in the sample application.
 */
object InitialData {

  def insert() {
    DB.withSession { implicit s: Session =>
      if(Eleves.count==0){
        Eleves.insert(Eleve(None,2014,"Nicolas","Blanc",Some("blanc.jpg"),Some("Nicololo")))
        Eleves.insert(Eleve(None,2014,"Benjamin","Brabant",Some("brabant.jpg"),Some("Buche")))
        Eleves.insert(Eleve(None,2014,"Bastien","Chares",Some("chares.jpg"),Some("Papy")))
        Eleves.insert(Eleve(None,2014,"Romain","De Oliveira",Some("deoliveira.jpg"),Some("Le Rouge")))
        Eleves.insert(Eleve(None,2014,"David","Dogneton",Some("dogneton.jpg"),Some("Double D")))
        Eleves.insert(Eleve(None,2014,"Nicolas","Dufau",Some("dufau.jpg"),Some("Marax")))
        Eleves.insert(Eleve(None,2014,"Thomas","Esterle",Some("esterle.jpg"),Some("Machine")))
        Eleves.insert(Eleve(None,2014,"Maxime","Gautre",Some("gautre.jpg"),Some("Maxor")))
        Eleves.insert(Eleve(None,2014,"Alexandre","Giraud",Some("giraud.jpg"),Some("LTP")))
        Eleves.insert(Eleve(None,2014,"Thibault","Goustat",Some("goustat.jpg"),Some("Thib")))
        Eleves.insert(Eleve(None,2014,"Thomas","Lenormand",Some("lenormand.jpg"),Some("Darken")))
        Eleves.insert(Eleve(None,2014,"Damien","Lescos",Some("lescos.jpg"),Some("Hibou")))
        Eleves.insert(Eleve(None,2014,"Kévin","Mascaret",Some("mascaret.jpg"),Some("Le Corse")))
        Eleves.insert(Eleve(None,2014,"Gaël","Zullo",Some("zullo.jpg"),None))
      }
      if(Projets.count==0){
        Seq(
          Projet(None,"Projet1",Some("http://www.eisti.fr"),None,"Lorem ipsum dolor sit amet, consectetuer adipiscing elit. Aenean commodo ligula eget dolor. Aenean massa. Cum sociis natoque penatibus et magnis dis parturient montes, nascetur ridiculus mus. Donec quam felis, ultricies nec, pellentesque eu, pretium quis, sem. Nulla consequat massa quis enim. Donec pede justo, fringilla vel, aliquet nec, vulputate eget, arcu. In enim justo, rhoncus ut, imperdiet a, venenatis vitae, justo. Nullam dictum felis eu pede mollis pretium"),
          Projet(None,"Projet2",None,None,"Je ne suis pas inspiré."),
          Projet(None,"Projet3",Some("http://www.eisti.fr"),None,"Lorem ipsum dolor sit amet, consectetuer adipiscing elit. Aenean commodo ligula eget dolor. Aenean massa. Cum sociis natoque penatibus et magnis dis parturient montes, nascetur ridiculus mus. Donec quam felis, ultricies nec, pellentesque eu, pretium quis, sem. Nulla consequat massa quis enim. Donec pede justo, fringilla vel, aliquet nec, vulputate eget, arcu. In enim justo, rhoncus ut, imperdiet a, venenatis vitae, justo. Nullam dictum felis eu pede mollis pretiumLorem ipsum dolor sit amet, consectetuer adipiscing elit. Aenean commodo ligula eget dolor. Aenean massa. Cum sociis natoque penatibus et magnis dis parturient montes, nascetur ridiculus mus. Donec quam felis, ultricies nec, pellentesque eu, pretium quis, sem. Nulla consequat massa quis enim. Donec pede justo, fringilla vel, aliquet nec, vulputate eget, arcu. In enim justo, rhoncus ut, imperdiet a, venenatis vitae, justo. Nullam dictum felis eu pede mollis pretiumLorem ipsum dolor sit amet, consectetuer adipiscing elit. Aenean commodo ligula eget dolor. Aenean massa. Cum sociis natoque penatibus et magnis dis parturient montes, nascetur ridiculus mus. Donec quam felis, ultricies nec, pellentesque eu, pretium quis, sem. Nulla consequat massa quis enim. Donec pede justo, fringilla vel, aliquet nec, vulputate eget, arcu. In enim justo, rhoncus ut, imperdiet a, venenatis vitae, justo. Nullam dictum felis eu pede mollis pretiumLorem ipsum dolor sit amet, consectetuer adipiscing elit. Aenean commodo ligula eget dolor. Aenean massa. Cum sociis natoque penatibus et magnis dis parturient montes, nascetur ridiculus mus. Donec quam felis, ultricies nec, pellentesque eu, pretium quis, sem. Nulla consequat massa quis enim. Donec pede justo, fringilla vel, aliquet nec, vulputate eget, arcu. In enim justo, rhoncus ut, imperdiet a, venenatis vitae, justo. Nullam dictum felis eu pede mollis pretiumLorem ipsum dolor sit amet, consectetuer adipiscing elit. Aenean commodo ligula eget dolor. Aenean massa. Cum sociis natoque penatibus et magnis dis parturient montes, nascetur ridiculus mus. Donec quam felis, ultricies nec, pellentesque eu, pretium quis, sem. Nulla consequat massa quis enim. Donec pede justo, fringilla vel, aliquet nec, vulputate eget, arcu. In enim justo, rhoncus ut, imperdiet a, venenatis vitae, justo. Nullam dictum felis eu pede mollis pretiumLorem ipsum dolor sit amet, consectetuer adipiscing elit. Aenean commodo ligula eget dolor. Aenean massa. Cum sociis natoque penatibus et magnis dis parturient montes, nascetur ridiculus mus. Donec quam felis, ultricies nec, pellentesque eu, pretium quis, sem. Nulla consequat massa quis enim. Donec pede justo, fringilla vel, aliquet nec, vulputate eget, arcu. In enim justo, rhoncus ut, imperdiet a, venenatis vitae, justo. Nullam dictum felis eu pede mollis pretiumLorem ipsum dolor sit amet, consectetuer adipiscing elit. Aenean commodo ligula eget dolor. Aenean massa. Cum sociis natoque penatibus et magnis dis parturient montes, nascetur ridiculus mus. Donec quam felis, ultricies nec, pellentesque eu, pretium quis, sem. Nulla consequat massa quis enim. Donec pede justo, fringilla vel, aliquet nec, vulputate eget, arcu. In enim justo, rhoncus ut, imperdiet a, venenatis vitae, justo. Nullam dictum felis eu pede mollis pretiumLorem ipsum dolor sit amet, consectetuer adipiscing elit. Aenean commodo ligula eget dolor. Aenean massa. Cum sociis natoque penatibus et magnis dis parturient montes, nascetur ridiculus mus. Donec quam felis, ultricies nec, pellentesque eu, pretium quis, sem. Nulla consequat massa quis enim. Donec pede justo, fringilla vel, aliquet nec, vulputate eget, arcu. In enim justo, rhoncus ut, imperdiet a, venenatis vitae, justo. Nullam dictum felis eu pede mollis pretium"),
          Projet(None,"Projet4",Some("http://www.eisti.fr"),None,"Lorem ipsum dolor sit amet, consectetuer adipiscing elit. Aenean commodo ligula eget dolor. Aenean massa. Cum sociis natoque penatibus et magnis dis parturient montes, nascetur ridiculus mus. Donec quam felis, ultricies nec, pellentesque eu, pretium quis, sem. Nulla consequat massa quis enim. Donec pede justo, fringilla vel, aliquet nec, vulputate eget, arcu. In enim justo, rhoncus ut, imperdiet a, venenatis vitae, justo. Nullam dictum felis eu pede mollis pretium"),
          Projet(None,"Projet5",Some("http://www.eisti.fr"),None,"Lorem ipsum dolor sit amet, consectetuer adipiscing elit. Aenean commodo ligula eget dolor. Aenean massa. Cum sociis natoque penatibus et magnis dis parturient montes, nascetur ridiculus mus. Donec quam felis, ultricies nec, pellentesque eu, pretium quis, sem. Nulla consequat massa quis enim. Donec pede justo, fringilla vel, aliquet nec, vulputate eget, arcu. In enim justo, rhoncus ut, imperdiet a, venenatis vitae, justo. Nullam dictum felis eu pede mollis pretium") ,
          Projet(None,"Projet6",Some("http://www.eisti.fr"),None,"Lorem ipsum dolor sit amet, consectetuer adipiscing elit. Aenean commodo ligula eget dolor. Aenean massa. Cum sociis natoque penatibus et magnis dis parturient montes, nascetur ridiculus mus. Donec quam felis, ultricies nec, pellentesque eu, pretium quis, sem. Nulla consequat massa quis enim. Donec pede justo, fringilla vel, aliquet nec, vulputate eget, arcu. In enim justo, rhoncus ut, imperdiet a, venenatis vitae, justo. Nullam dictum felis eu pede mollis pretium"),
          Projet(None,"Projet7",Some("http://www.eisti.fr"),None,"Lorem ipsum dolor sit amet, consectetuer adipiscing elit. Aenean commodo ligula eget dolor. Aenean massa. Cum sociis natoque penatibus et magnis dis parturient montes, nascetur ridiculus mus. Donec quam felis, ultricies nec, pellentesque eu, pretium quis, sem. Nulla consequat massa quis enim. Donec pede justo, fringilla vel, aliquet nec, vulputate eget, arcu. In enim justo, rhoncus ut, imperdiet a, venenatis vitae, justo. Nullam dictum felis eu pede mollis pretium"),
          Projet(None,"Projet8",Some("http://www.eisti.fr"),None,"Lorem ipsum dolor sit amet, consectetuer adipiscing elit. Aenean commodo ligula eget dolor. Aenean massa. Cum sociis natoque penatibus et magnis dis parturient montes, nascetur ridiculus mus. Donec quam felis, ultricies nec, pellentesque eu, pretium quis, sem. Nulla consequat massa quis enim. Donec pede justo, fringilla vel, aliquet nec, vulputate eget, arcu. In enim justo, rhoncus ut, imperdiet a, venenatis vitae, justo. Nullam dictum felis eu pede mollis pretium")
        ).foreach(Projets.insert)
      }
      if(Accounts.count==0){
        Seq(
          Account(None,"fd@eisti.eu","12345"),
          Account(None,"yl@eisti.eu","12345")
        ).foreach(Accounts.insert)
      }
      if(Professeurs.count==0){
        Seq(
        Professeur(None,"Florent","Devin",Some("florent.devin@eisti.eu"),Some("+33 5 59 05 90 70"),Some("fd.jpg"),Some("Co-directeur du département Informatique<br/>Co-directeur de l'option ICC"),Some("Informatique")),
        Professeur(None,"Yannick","Le Nir",Some("yannick.lenir@eisti.eu"),Some("+33 5 59 05 90 71"),Some("yl.jpg"),Some("Co-directeur du département Informatique<br/>Co-directeur de l'option ICC"),Some("Informatique"))
        ).foreach(Professeurs.insert)
      }
    }
  }
}