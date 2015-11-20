package controllers

import models._
import play.api.mvc.Controller
import jp.t2v.lab.play2.auth.AuthenticationElement
import play.api.data._
import play.api.data.Forms._
import play.api.data.format.Formats._
import models.Projet
import scala.Some
import play.api.Logger
import com.sksamuel.scrimage.{Format, Image}
import java.awt.Color


object ProjetsController extends Controller with AuthenticationElement with AuthConfigImpl {

  val ProjetForm = Form(
    mapping(
      "id" ->optional(of[Long]),
      "titre" -> nonEmptyText,
      "url" -> optional(of[String]),
      "description" -> nonEmptyText,
      "oldphoto"->optional(of[String])
    ){
      (id,titre,url,description,oldphoto) => Projet(id,titre,url,oldphoto,description)
    } {
      projet=> Some(projet.id,projet.titre,projet.url,projet.description,projet.imagePath)
    }
  )

  def deleteProjet(id: Long) = StackAction{
    implicit request =>
      Projets.delete(id)
      Redirect(routes.Application.admin).flashing("success"->"Projet supprimé avec succès")
  }

  def submitProjet = StackAction(parse.multipartFormData){
    implicit request =>
      ProjetForm.bindFromRequest.fold(
        formWithErrors => {
          Logger.debug("Projet mal renseigné")
          Logger.debug(s"Bad registration !! : ${formWithErrors}")
          Redirect(routes.Application.admin)
        },
        projet => {
          Logger.debug("Projet bien renseigné")
          request.body.file("photo").map {
            picture =>
              import java.io.File
              val filename = "temp"
              val contentType = picture.contentType
              Logger.debug("test photo " +filename)
              val newProjet=Projet(projet.id,projet.titre,projet.url,Some(projet.id+".png"),projet.description)
              if(newProjet.id.isDefined){
                picture.ref.moveTo(new File("public/images/projets/" +filename),true)
                val image = new File("public/images/projets/" +filename)
                Image(image).fit(1000,400).autocrop(Color.white).padTo(1000,400,new Color(0,0,0,0)).write(new File("public/images/projets/"+projet.id+".png"),Format.PNG)
                image.delete()
                Projets.update(newProjet.id.get,newProjet)
                Redirect(routes.Application.admin).flashing("success"->"Projet mis à jour avec succès")
              }
              else {
                val newProjetId = Projets.insert(newProjet)
                picture.ref.moveTo(new File("public/images/projets/" +filename),true)
                val image = new File("public/images/projets/" +filename)
                Image(image).fit(1000,400).autocrop(Color.white).padTo(1000,400,new Color(0,0,0,0)).write(new File("public/images/projets/"+Some(newProjetId)+".png"),Format.PNG)
                image.delete()
                val newProjetTemp=Projet(Some(newProjetId),projet.titre,projet.url,Some(Some(newProjetId)+".png"),projet.description)
                Projets.update(newProjetId,newProjetTemp)
                Redirect(routes.Application.admin).flashing("success"->"Projet inséré avec succès")
              }

          }.getOrElse {
            Logger.debug("test pas de photo " )
            if(projet.id.isDefined){
              Projets.update(projet.id.get,projet)
               Redirect(routes.Application.admin).flashing("success"->"Projet mis à jour avec succès")
            }
            else {
              Projets.insert(projet)
              Redirect(routes.Application.admin).flashing("success"->"Projet inséré avec succès")
            }
          }
        }
      )
  }
}
