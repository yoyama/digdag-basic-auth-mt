package io.github.yoyama.digdag.bauth


import com.google.inject.Inject
import io.digdag.spi.{Authenticator, AuthenticatorFactory, Plugin}

class BasicAuthMTAuthenticatorPlugin @Inject() (/**systemConfig: Config*/) extends Plugin {
  override def getServiceProvider[T](t: Class[T]): Class[_ <: T] = {
    if (t eq classOf[Authenticator]) {
      classOf[BasicAuthMTAuthenticator].asSubclass(t)
    }
    else if (t eq classOf[AuthenticatorFactory]) {
      classOf[BasicAuthMTAuthenticatorFactory].asSubclass(t)
    }
    else {
      null
    }
  }
}
