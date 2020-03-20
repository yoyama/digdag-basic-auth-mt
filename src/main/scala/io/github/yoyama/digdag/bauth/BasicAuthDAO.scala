package io.github.yoyama.digdag.bauth
import scala.jdk.CollectionConverters._
import java.nio.charset.Charset
import java.nio.file.{Files, Path}
import java.util.Base64

import org.apache.commons.codec.digest.Md5Crypt

case class UserInfo(user:String, passwd:String, siteId:Int, admin:Boolean)
trait BasicAuthDAO {
  def authorize(authHeader: Option[String]): Option[UserInfo]
}

class BasicAuthFileDAO(val passwdFile:Path, val userMapFile:Path) extends BasicAuthDAO {
  val UTF8 = Charset.forName("UTF-8")
  val userInfo:Map[String, UserInfo] = readUserInfo(passwdFile, userMapFile)

  override def authorize(authHeader: Option[String]): Option[UserInfo] = {
    parseAuthorizationHeader(authHeader)
      .flatMap(u => userInfo.get(u._1).map(x=>(x, u._2))) // Option(UserInfo, passwd)
      .flatMap(u => if(checkPasswd(u._2, u._1.passwd)) Some(u._1) else None)
  }

  def checkPasswd(rawPasswd:String, encPasswd:String):Boolean = {
    val regApr1 = """\$apr1\$([^\$]+)\$(.*)""".r

    encPasswd match {
      case regApr1(s, p) => {
        val p2 = Md5Crypt.apr1Crypt(rawPasswd.getBytes, s)
        p2 == encPasswd
      }
      case _ => {
        false
      }
    }
  }

  // Parse Authorization header.
  def parseAuthorizationHeader(header:Option[String]): Option[(String,String)] = {
    val baRegex = """Basic¥s+(¥S+)""".r
    header match {
      case Some(baRegex(v)) => {
        val h = new String(Base64.getDecoder.decode(v.getBytes(UTF8)))
        val ar = h.split(":", 2)
        if(ar.size == 2) Some((ar(0), ar(1))) else None
      }
      case _ => None
    }
  }

  def readUserInfo(passwdFile:Path, userFile:Path): Map[String, UserInfo] = {
    val passwds: Map[String, String] = Files.readAllLines(passwdFile, UTF8).asScala
      .map(_.split(":", 2))
      .filter(_.length == 2)
      .map(x => (x(0), x(1))).toMap
    val users:Map[String, (Int, Boolean)] = Files.readAllLines(userFile, UTF8).asScala
      .map(_.split(":", 3))
      .filter(_.length == 3)
      .map(x=>(x(0), (x(1).toInt, (x(2).toBoolean)))).toMap
    passwds.keys.toSeq.intersect(users.keys.toSeq)
      .map(x => {
        val u = users.get(x)
        val sid = if(u.isDefined) u.get._1 else -1
        val passwd = passwds.get(x).getOrElse("")
        val admin = if(u.isDefined) u.get._2 else false
        (x, UserInfo(user = x, passwd = passwd, siteId = sid, admin = admin))
      })
      .toMap
  }
}
