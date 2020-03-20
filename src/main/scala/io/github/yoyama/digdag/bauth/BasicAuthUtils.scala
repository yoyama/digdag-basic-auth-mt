package io.github.yoyama.digdag.bauth

import org.apache.commons.codec.digest.Md5Crypt

class BasicAuthUtils {
  def md5(text: String): String = {
    Md5Crypt.apr1Crypt(text.getBytes, "/oiXAMY7")
    //java.security.MessageDigest.getInstance("MD5").digest(text.getBytes).map("%02x".format(_)).mkString
  }

}
