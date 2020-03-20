package io.github.yoyama.digdag.bauth

import java.util.Optional

import com.google.inject.{Binder, Inject, TypeLiteral}
import io.digdag.client.config.Config
import io.digdag.spi.{Authenticator, Plugin}

class BasicAuthMTAuthenticatorPlugin @Inject() (/**systemConfig: Config*/) extends Plugin {
  override def getServiceProvider[T](t: Class[T]): Class[_ <: T] = {
    println("BasicAuthMTAuthenticatorPlugin.getServiceProvider() called")
    if (t eq classOf[Authenticator]) {
      println("return  BasicAuthMTAuthenticator")
      classOf[BasicAuthMTAuthenticator].asSubclass(t)
    }
    else {
      null
    }
  }

  override def configureBinder[T](t: Class[T], binder: Binder): Unit = {
    //binder.bind(new TypeLiteral[Optional[BasicAuthenticatorConfig]]() {}).toProvider(classOf[BasicAuthenticatorConfigProvider]).asEagerSingleton()
  }

}
