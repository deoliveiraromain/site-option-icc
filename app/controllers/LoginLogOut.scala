package controllers

import play.api.mvc.{Action, Controller}
import jp.t2v.lab.play2.auth.{OptionalAuthElement, LoginLogout}
import models.{Accounts, Permission, Account}
import play.api.data.Form
import play.api._
import play.api.mvc._
import play.api.data._
import play.api.data.Forms._
import play.api.data.validation._
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

object LoginLogOut extends Controller with LoginLogout with OptionalAuthElement with AuthConfigImpl {
  val loginForm = Form(
    mapping(
      "id" ->ignored[Option[Long]](None),
      "email" -> text,
      "password" -> text
    ){
    (id, email, password) => Account(id, email,password)//, Permission.ADMINISTRATOR)
    }{
      account => Some(account.id, account.email,account.password)
  }
  )

      def connexion = StackAction {
        implicit request =>
          val maybeUser :Option[User] = loggedIn
          if(maybeUser.isDefined){
            Logger.debug("Deja Loggué !!!")
            Redirect(routes.Application.admin)
          }
        else{
           Ok(views.html.connexion(loginForm))
          }
      }

      def logout = Action.async {
        implicit request =>
        gotoLogoutSucceeded
      }


  def authenticate = Action.async {
    implicit request =>
    loginForm.bindFromRequest.fold(
      formWithErrors => {
        Logger.debug("Formulaire connexion mal rempli")
        Future.successful(BadRequest(views.html.connexion(formWithErrors)))},
      user => {
        Logger.debug("Formulaire connexion bien rempli")
        Accounts.authenticate(user.email,user.password).flatMap {
        current =>
          current match {
            case Some(_) => {
              Logger.debug("Administrateur Existant")
             gotoLoginSucceeded(user.email)
            }
            case None => {
              Accounts.findByEmail(user.email).flatMap{
                current =>
                  current match {
                  case Some(_) => {
                  Logger.debug("L'auth n'a pas marché, mais le compte mail existe donc mauvais mot de passe")
                  Future.successful(Redirect(routes.LoginLogOut.connexion).flashing("error"->s" Le mot de passe est incorrect pour le compte ${user.email}"))
                }
                case None => {
                 Logger.debug("L'auth n'a pas marché, et le compte mail existe pas donc le compte n'existe pas")
                 authorizationFailed(request)
                }
              }
              }
            }
          }
      }
      }
    )
  }

}
