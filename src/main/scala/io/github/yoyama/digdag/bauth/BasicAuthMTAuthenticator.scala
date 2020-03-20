package io.github.yoyama.digdag.bauth

import java.nio.file.{FileSystems, Path, Paths}

import com.google.inject.Inject
import io.digdag.client.config.Config
import io.digdag.spi.Authenticator
import io.digdag.spi.Authenticator.Result
import javax.ws.rs.container.ContainerRequestContext
import org.slf4j.{Logger, LoggerFactory}

class BasicAuthMTAuthenticator @Inject() (systemConfig:Config) extends Authenticator {
  private val logger = LoggerFactory.getLogger(classOf[BasicAuthMTAuthenticator])
  private val passwdFileName:String = systemConfig.get("basicauth_mt.passwd_file", classOf[String])
  private val userMapFileName:String = systemConfig.get("basicauth_mt.user_map_file", classOf[String])
  private val dao:BasicAuthDAO = new BasicAuthFileDAO(toPath(passwdFileName),toPath(userMapFileName))

  override def authenticate(requestContext: ContainerRequestContext): Authenticator.Result = {
    dao.authorize(Option(requestContext.getHeaderString("Authorization"))) match {
      case Some(userInfo) => Result.builder.isAdmin(userInfo.admin).siteId(userInfo.siteId).build
      case _ => {
        Result.reject("Failed to authorize")
      }
    }
  }

  private def toPath(fname:String):Path = FileSystems.getDefault().getPath(fname)
}
