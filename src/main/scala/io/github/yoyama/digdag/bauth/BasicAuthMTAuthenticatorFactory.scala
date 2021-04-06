package io.github.yoyama.digdag.bauth

import com.google.inject.Inject
import io.digdag.client.config.{Config, ConfigFactory}
import io.digdag.spi.{Authenticator, AuthenticatorFactory}

class BasicAuthMTAuthenticatorFactory @Inject()(systemConfig: Config, cf: ConfigFactory) extends AuthenticatorFactory {
  override def getType: String = "basic_mt"

  override def newAuthenticator(): Authenticator = {
    new BasicAuthMTAuthenticator(systemConfig, cf)
  }
}
