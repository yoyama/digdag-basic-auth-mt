# digdag-basic-auth-mt
[ ![Download](https://api.bintray.com/packages/yoyama/maven/digdag-basic-auth-mt/images/download.svg?version=0.1.0) ](https://bintray.com/yoyama/maven/digdag-basic-auth-mt/0.1.0/link)

Multi tenant basic authentication plugin for Digdag.

## Insatll

### Server configuraitotn
```
server.authenticator-class=io.github.yoyama.digdag.bauth.BasicAuthMTAuthenticator
system-plugin.repositories=https://dl.bintray.com/yoyama/maven
system-plugin.dependencies=io.github.yoyama:digdag-basic-auth-mt_2.12:0.1.0
plugins.basic_auth_mt.passwd_file=<path_to_passwrwd_file>
plugins.basic_auth_mt.user_map_file=<path_to_user_map_fiel>
```

### Password file
Password file stores user/password mapping informatoin.
You can create with htpasswd command.
```
$ htpasswd -c passwd.txt test1
New password:
Re-type new password:
Adding password for user test1

$ cat passwd.txt
test1:$apr1$sc6mDosk$EplIWYLPLlhRybr5MuiIx/

```

### User map file
User map file stores users and its site id. In addition a flag for admin. Format is _user:site_id:admin_ .
An example is as follows.
```
test1:1:true
test2:21:false
```

## Use basic authentication from CLI

digdag CLI support _--basic-auth_ as follows.

```
$ digdag projects --basic-auth test1:aaaa
2020-03-22 22:43:48 +0900: Digdag v0.9.41
error: Status code 401: HTTP 401 Unauthorized

```
