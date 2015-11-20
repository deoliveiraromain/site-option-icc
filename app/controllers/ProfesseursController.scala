package controllers

import models._
import play.api.mvc.Controller
import jp.t2v.lab.play2.auth.AuthenticationElement
import play.api.data._
import play.api.data.Forms._
import play.api.data.format.Formats._
import play.api.Logger
import com.sksamuel.scrimage.{Format, Image}
import java.awt.Color


object ProfesseursController extends Controller with AuthenticationElement with AuthConfigImpl {

    val ProfesseurForm = Form(
      mapping(
        "id" ->optional(of[Long]),
        "prenom" -> nonEmptyText,
        "nom" -> nonEmptyText,
        "email" ->optional(of[String]),
        "telephone" ->optional(of[String]),
        "description" -> optional(of[String]),
        "departement" -> optional(of[String]),
        "oldphoto"->optional(of[String])
      ){
        (id,prenom,nom,email,telephone,description,departement,oldphoto) => Professeur(id,prenom,nom,email,telephone,oldphoto,description,departement)
      } {
        prof=> Some(prof.id,prof.prenom,prof.nom,prof.email,prof.telephone,prof.description,prof.departement,prof.photo)
      }
    )

    def deleteProf(id: Long) = StackAction{
      implicit request =>
        Professeurs.delete(id)
        Redirect(routes.Application.admin).flashing("success"->"Professeur supprimé  avec succès")

    }

  def submitProf = StackAction(parse.multipartFormData){
    implicit request =>
      ProfesseurForm.bindFromRequest.fold(
        formWithErrors => {
          Logger.debug("Prof mal renseigné")
          Logger.debug(s"Bad registration !! : ${formWithErrors}")
          Redirect(routes.Application.admin)
        },
        prof => {
          Logger.debug("Prof bien renseigné")
          request.body.file("photo").map {
            picture =>
              import java.io.File
              val filename = "temp"
              val contentType = picture.contentType
              Logger.debug("test photo " +filename)
              val newProf=Professeur(prof.id,prof.prenom,prof.nom,prof.email,prof.telephone,Some(prof.id+".png"),prof.description,prof.departement)
              if(newProf.id.isDefined){
                picture.ref.moveTo(new File("public/images/profs/" +filename),true)
                val image = new File("public/images/profs/" +filename)
                Image(image).fit(156,206).autocrop(Color.white).padTo(156,206,new Color(0,0,0,0)).write(new File("public/images/profs/"+prof.id+".png"),Format.PNG)
                image.delete()
                Professeurs.update(newProf.id.get,newProf)
                Redirect(routes.Application.admin).flashing("success"->"Professeur mis à jour avec succès")
              }
              else {
                val newProfId = Professeurs.insert(newProf)
                picture.ref.moveTo(new File("public/images/profs/" +filename),true)
                val image = new File("public/images/profs/" +filename)
                Image(image).fit(156,206).autocrop(Color.white).padTo(156,206,new Color(0,0,0,0)).write(new File("public/images/profs/"+Some(newProfId)+".png"),Format.PNG)
                image.delete()
                val newProfTemp=Professeur(Some(newProfId),prof.prenom,prof.nom,prof.email,prof.telephone,Some(Some(newProfId)+".png"),prof.description,prof.departement)
                Professeurs.update(newProfId,newProfTemp)
                Redirect(routes.Application.admin).flashing("success"->"Professeur inséré avec succès")
              }

          }.getOrElse {
            Logger.debug("test pas de photo " )
            if(prof.id.isDefined){
              Professeurs.update(prof.id.get,prof)
              Redirect(routes.Application.admin).flashing("success"->"Professeur mis à jour avec succès")
            }
            else {
              Professeurs.insert(prof)
              Redirect(routes.Application.admin).flashing("success"->"Professeur inséré avec succès")
            }
          }
        }
      )
  }
 }


