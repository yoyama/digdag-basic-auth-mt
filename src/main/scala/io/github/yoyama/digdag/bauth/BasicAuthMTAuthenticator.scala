package io.github.yoyama.digdag.bauth


import java.nio.file.{FileSystems, Path, Paths}
import com.google.inject.Inject
import io.digdag.client.config.{Config, ConfigFactory}
import io.digdag.spi.{AuthenticatedUser, Authenticator}
import io.digdag.spi.Authenticator.Result

import javax.ws.rs.container.ContainerRequestContext
import org.slf4j.{Logger, LoggerFactory}


class BasicAuthMTAuthenticator @Inject() (systemConfig:Config, cf: ConfigFactory) extends Authenticator {
  private val logger = LoggerFactory.getLogger(classOf[BasicAuthMTAuthenticator])
  private val passwdFileName:String = systemConfig.get("plugins.basic_auth_mt.passwd_file", classOf[String])
  private val userMapFileName:String = systemConfig.get("plugins.basic_auth_mt.user_map_file", classOf[String])
  private val dao:BasicAuthDAO = new BasicAuthFileDAO(toPath(passwdFileName),toPath(userMapFileName))

  def toDigdagAutenticatedUser(userInfo:UserInfo):AuthenticatedUser = {
    AuthenticatedUser.builder()
      .siteId(userInfo.siteId)
      .isAdmin(userInfo.admin)
      .userInfo(cf.create())
      .userContext(cf.create())
      .build()
  }

  override def authenticate(requestContext: ContainerRequestContext): Authenticator.Result = {
    dao.authorize(Option(requestContext.getHeaderString("Authorization"))) match {
      case Some(userInfo) => Result.accept(toDigdagAutenticatedUser(userInfo))
      case _              => Result.reject("Failed to authorize")
    }
  }

  private def toPath(fname:String):Path = FileSystems.getDefault().getPath(fname)
}
