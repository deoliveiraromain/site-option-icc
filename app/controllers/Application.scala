package controllers

import play.api._
import play.api.mvc._
import play.api.libs.oauth.{RequestToken, ServiceInfo, OAuth, ConsumerKey}
import models.{Permission}
import jp.t2v.lab.play2.auth.AuthElement
import models.Permission
import jp.t2v.lab.play2.auth.AuthenticationElement
import models._
import play.api.db.slick.DBAction
import play.api._
import play.api.mvc._
import play.api.data._
import play.api.data.Forms._
import play.api.db.slick._
import play.api.Play.current

object Application extends Controller with AuthenticationElement with AuthConfigImpl{

  def accueil = Action {
    Ok(views.html.index())
  }

  def presentation = Action {
    Ok(views.html.presentation())
  }

  def metiers = Action {
    Ok(views.html.metiers())
  }

  def professeurs = Action {
    Ok(views.html.professeurs(Professeurs.list))
  }

  def eleves = Action {
    Ok(views.html.eleves(Eleves.list,Eleves.listPromotions))
  }

  def projets = DBAction{
    implicit s =>
      Ok(views.html.projets(Projets.list))
  }

  def admin = StackAction {
    implicit request =>
      Ok(views.html.admin(Accounts.list,Projets.list,Eleves.list,Professeurs.list))
  }
}