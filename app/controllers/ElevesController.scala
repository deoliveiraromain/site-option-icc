package controllers

import models.{Eleve, Eleves}
import play.api.mvc.Controller
import jp.t2v.lab.play2.auth.AuthenticationElement
import play.api.data._
import play.api.data.Forms._
import play.api.data.format.Formats._
import play.api.Logger
import com.sksamuel.scrimage.{Format, Image}
import java.awt.Color


object ElevesController extends Controller with AuthenticationElement with AuthConfigImpl{
  val EleveForm = Form(
    mapping(
      "id" ->optional(of[Long]),
      "promotion" -> number,
      "prenom" -> nonEmptyText,
      "nom" -> nonEmptyText,
      "pseudo" -> optional(of[String]) ,
      "oldphoto"->optional(of[String])
    ) {
      (id, promotion,prenom,nom,pseudo,oldphoto) => Eleve(id,promotion,prenom,nom,oldphoto,pseudo)
    } {
      eleve=> Some(eleve.id, eleve.promotion,eleve.prenom,eleve.nom,eleve.photo,eleve.pseudo)
    }
  )

  def deleteEleve(id: Long) = StackAction{
    implicit request =>
      Eleves.delete(id)
      Redirect(routes.Application.admin).flashing("success"->"Elève supprimé avec succès")
  }

  def submitEleve = StackAction(parse.multipartFormData){
    implicit request =>
      EleveForm.bindFromRequest.fold(
        formWithErrors => {
          Logger.debug("Eleve mal renseigné")
          Logger.debug(s"Bad registration !! : ${formWithErrors}")
          Redirect(routes.Application.admin)
        },
        user => {
          Logger.debug("Eleve bien renseigné")
          request.body.file("photo").map {
            picture =>
            val newEleve=Eleve(user.id,user.promotion,user.prenom,user.nom,Some(user.id+".png"),user.pseudo)
              if(newEleve.id.isDefined){
                import java.io.File
                val filename = "temp"
                val contentType = picture.contentType
                Logger.debug("test photo " +filename)
                picture.ref.moveTo(new File("public/images/eleves/" +filename),true)
                val image = new File("public/images/eleves/" +filename)
                Image(image).fit(155,206).autocrop(Color.white).padTo(155,206,new Color(0,0,0,0)).write(new File("public/images/eleves/"+user.id+".png"),Format.PNG)
                image.delete()
                Eleves.update(newEleve.id.get,newEleve)
                Redirect(routes.Application.admin).flashing("success"->"Elève mis à jour avec succès")
              }
              else {
                val eleveNewId = Eleves.insert(newEleve)
                import java.io.File
                val filename = "temp"
                val contentType = picture.contentType
                Logger.debug("test photo " +filename)
                picture.ref.moveTo(new File("public/images/eleves/" +filename),true)
                val image = new File("public/images/eleves/" +filename)
                Image(image).fit(155,206).autocrop(Color.white).padTo(155,206,new Color(0,0,0,0)).write(new File("public/images/eleves/"+Some(eleveNewId)+".png"),Format.PNG)
                val eleveTemp = Eleve(Option(eleveNewId),newEleve.promotion,newEleve.prenom,newEleve.nom,Some(Some(eleveNewId)+".png"),newEleve.pseudo)
                image.delete()
                Eleves.update(eleveNewId,eleveTemp)
                Redirect(routes.Application.admin).flashing("success"->"Elève inséré avec succès")
              }
          }.getOrElse {
            //Pas de fichier fourni on garde l'ancienne photo dans le cas d'un update
            Logger.debug("test pas de photo " )
            if(user.id.isDefined){
              Eleves.update(user.id.get,user)
              Redirect(routes.Application.admin).flashing("success"->"Elève mis à jour avec succès")
            }
            else {
              Eleves.insert(user)
              Redirect(routes.Application.admin).flashing("success"->"Elève inséré avec succès")
            }
          }
        }
      )
  }
}
