package io.github.yoyama.digdag.bauth


import com.google.inject.{Binder, Inject, TypeLiteral}
import io.digdag.spi.{Authenticator, Plugin}

class BasicAuthMTAuthenticatorPlugin @Inject() (/**systemConfig: Config*/) extends Plugin {
  override def getServiceProvider[T](t: Class[T]): Class[_ <: T] = {
    if (t eq classOf[Authenticator]) {
      classOf[BasicAuthMTAuthenticator].asSubclass(t)
    }
    else {
      null
    }
  }
}
