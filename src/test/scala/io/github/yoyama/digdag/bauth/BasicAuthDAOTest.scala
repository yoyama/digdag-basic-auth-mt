package io.github.yoyama.digdag.bauth

import java.nio.charset.Charset
import java.nio.file.{Files, Paths, StandardOpenOption}

import wvlet.airspec._

class BasicAuthFileDAOTest extends AirSpec {
    val tmpDir = Files.createTempDirectory("BasicAuthDAOTest")
    val passwdFile = Files.write(Paths.get(tmpDir.toFile.getAbsolutePath, "passwd.txt"),
        "yoyama:$apr1$26Vr1CPR$ELl2eTQuvHR.6y9XSy8v10".getBytes(Charset.forName("UTF-8")))
    val userMapFile = Files.write(Paths.get(tmpDir.toFile.getAbsolutePath, "users.txt"),
        "yoyama:11:true".getBytes(Charset.forName("UTF-8")))

    val dao = new BasicAuthFileDAO(passwdFile, userMapFile)

    def readUserInfoTest: Unit = {
        println(dao.userInfo)
        assert(dao.userInfo.size == 1)
        assert(dao.userInfo.get("yoyama") == Some(UserInfo("yoyama", "$apr1$26Vr1CPR$ELl2eTQuvHR.6y9XSy8v10", 11, true)))
    }

    def checkPasswdTest: Unit = {
        assert(dao.checkPasswd("test", "$apr1$26Vr1CPR$ELl2eTQuvHR.6y9XSy8v10"))
        assert(!dao.checkPasswd("hoge", "$apr1$26Vr1CPR$ELl2eTQuvHR.6y9XSy8v10"))
    }

    def parseAuthorizationHeaderTest() = {
        val d = "Basic eW95YW1hOnRlc3Q="
        val x = dao.parseAuthorizationHeader(Some(d))
        assert(x.isDefined)
        assert(x.get._1 == "yoyama")
        assert(x.get._2 == "test")
    }
}
