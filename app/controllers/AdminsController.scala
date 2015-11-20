package controllers

import models._
import play.api.mvc.Controller

import play.api.data._
import play.api.data.Forms._
import play.api.data.format.Formats._
import play.api.Logger
import jp.t2v.lab.play2.auth.{LoginLogout, AuthenticationElement}

object AdminsController extends Controller with LoginLogout with AuthenticationElement  with AuthConfigImpl {

  val AdminForm = Form(
    mapping(
      "id" ->optional(of[Long]),
      "login" -> nonEmptyText,
      "password" -> nonEmptyText
    ){
      (id,login,password) => Account(id,login,password)
    } {
      admin=> Some(admin.id,admin.email,admin.password)
    }
  )

  def deleteAdmin(id: Long) = StackAction{
    implicit request =>
      val maybeUser :User = loggedIn
      if(id==maybeUser.id.get){
        Redirect(routes.Application.admin).flashing("error"->"Vous ne pouvez pas supprimer le compte admin avec lequel vous êtes logué !")
      }
    else {
      Accounts.delete(id)
      Redirect(routes.Application.admin).flashing("success"->"Administrateur supprimé avec succès")
    }
  }

  def submitAdmin = StackAction{
    implicit request =>
      AdminForm.bindFromRequest.fold(
        formWithErrors => {
          Logger.debug("Admin mal renseigné")
          Logger.debug(s"Bad registration !! : ${formWithErrors}")
          Redirect(routes.Application.admin)
        },
        admin => {
          Logger.debug("Admin bien renseigné")

            if(admin.id.isDefined){
              Accounts.update(admin.id.get,admin)
              Redirect(routes.Application.admin).flashing("success"->"Administrateur mis à jour avec succès")
            }
            else {
              Accounts.insert(admin)
              Redirect(routes.Application.admin).flashing("success"->"Administrateur inséré avec succès")
            }
          }
      )
  }
}


