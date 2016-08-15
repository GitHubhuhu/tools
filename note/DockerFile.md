# Dockerfile 笔记

Dockerfile 用来构建镜像的，他有自己的语法和规则。

Dockerfile每一条指令都会提交一个新的镜像，下一个指令基于上一个指令的镜像构建。

[官网地址](https://docs.docker.com/engine/reference/builder/)

[Docker 中文](http://www.docker.org.cn/)


## 指令

Dockerfile所有的指令请使用大写字母（约定）

> FROM
	
指令：

1. FROM image  
2. FROM image:tag

	
说明要创建的镜像基于哪个镜像，一个Dockerfile可以有多个FROM指令,表示创建多个镜像。FROM镜像时首先从本地查找镜像，如果不存在则在公共仓库查找。也可以指定私有仓库。

eg:

```
FROM busybox
FROM 私有仓库地址/busybox
FROM busybox:1.0
	
```


> MAINTAINER 

指令：
	MAINTAINER  authorName

指定镜像的作者

eg:
	
```
MAINTAINER "lu@funguide.com.cn/ludada"
	
```

> USER 

指令：

1. USER uid 
2. USER username

指定运行容器的用户

eg:

```
USER root
USER 119
	
```
> EXPOSE 

指令：EXPOSE [port]

容器运行时需要监听的端口，这种方法指定主要用于容器之间通信(`links`)，宿主机是访问不到的。如果要暴露给宿主机，则启动容器时需要 `-p`参数，可以暴露多个端口。
  
```
与docker run --expose[] 作用一样。
docker run -P 将所有expose端口到随机映射到主机，
docker run -p 容器端口 是指定容器expose的端口随机映射到主机，
docker run -p 主机端口:容器端口 指定容器expose的端口映射到主机固定的端口。端口映射默认使用的协议是tcp，如果要使用udp 则需要 -p 主机端口:容器端口/udp
docker run --link[] 还不太懂
```
  
eg:
  
```
EXPOSE 1111
EXPOSE 2222
也可以：
EXPOSE 1111 2222
```
  
  
> ENV
  	
指令：ENV key value

设置环境变量
  	
eg: 
	
```
ENV LANG en_US.UTF-8
ENV LC_ALL en_US.UTF-8

使用环境变量： $LANG
	
```  
	


> RUN 


1. RUN command 在shell终端运行命令
2. RUN ["executable", "param1", "param2”] 使用exec执行命令

RUN ["程序名", "参数1", "参数2"]

RUN指令会在当前镜像的顶层执行任何命令，并commit成新的（中间）镜像，提交的镜像会在后面继续用到。可以用来安装程序、编译代码等等。

```
shell 格式，相当于执行 /bin/sh -c "<command>"
exec 格式，不会触发shell，所以$HOME这样的环境变量无法使用，但它可以在没有bash的镜像中执行，而且可以避免错误的解析命令字符串
```
 
eg:
 	
 	```
 	RUN /bin/bash -c 'source $HOME/.bashrc ; echo $HOME'
 	
 	RUN ["apt-get", "install", "vim", "-y"]
 	或者
 	RUN ["/bin/bash", "-c", "apt-get install vim -y"]  与shell风格相同

 	```
> CMD

指令：

1. CMD ["executable","param1","param2"] 使用 exec 执行，推荐方式；
2. CMD command param1 param2 在 /bin/sh 中执行，提供给需要交互的应用；
3. CMD ["param1","param2"] 提供给 ENTRYPOINT 的默认参数；

CMD指令的主要功能是在build完成后，为了给docker run启动到容器时提供默认命令或参数，这些默认值可以包含可执行的命令，也可以只是参数（此时可执行命令就必须提前在ENTRYPORINT中指定）。

它与ENTRYPOINT的功能极为相似，区别在于如果docker run后面出现与CMD指定的相同命令，那么CMD会被覆盖；而ENTRYPOINT会把容器名后面的所有内容都当成参数传递给其指定的命令（不会对命令覆盖）。另外CMD还可以单独作为ENTRYPOINT的所接命令的可选参数。
CMD与RUN的区别在于，RUN是在build成镜像时就运行的，先于CMD和ENTRYPOINT的，CMD会在每次启动容器的时候运行，而RUN只在创建镜像时执行一次，固化在image中。

eg:

```
Dockerfile:
    CMD ["echo CMD_args"]
运行
    docker run <image> echo run_arg
结果
    输出 run_arg
   
>>> 因为echo run_arg覆盖了CMD。如果run后没有echo run_arg，则输出CMD_args。
    
```
eg:

```
Dockerfile:
    ENTRYPOINT ["echo", "ENTRYPOINT_args"]
运行
    docker run <image> echo run_arg
结果
    输出 ENTRYPOINT_args run_arg
    
>>> 因为echo run_arg追加到ENTRYPOIINT的echo后面了。
    如果在ENTRYPOINT后再加入一行CMD ["CMD_args"]，则结果依旧，除非去掉run后的所有参数。
    当出现ENTRYPOINT指令时CMD指令只可能(当ENTRYPOINT指令使用exec方式执行时)
    被当做ENTRYPOINT指令的参数使用，其他情况则会被忽略。


```


     
> ENTRYPOINT
指令：

1. ENTRYPOINT ["executable", "param1", "param2"]  exec格式，推荐
2. ENTRYPOINT command param1 param2（shell中执行）。

让容器就像一个可执行程序一样，容器启动时执行。但一个dockerfile 只能执行一条，如果有多条，则执行最后一条。不被docker run 提供的参数覆盖。使用exec格式，在docker run <image>的所有参数，都会追加到ENTRYPOINT之后，并且会覆盖CMD所指定的参数（如果有的话）。当然可以在run时使用--entrypoint来覆盖ENTRYPOINT指令。
使用shell格式，ENTRYPOINT相当于执行/bin/sh -c <command..>，这种格式会忽略docker run和CMD的所有参数。

一种常用的设置是将命令与必要参数设置到ENTRYPOINT中，而运行时只提供其他选项。例如：你有一个MySQL的客户端程序运行在容器中，而客户端所需要的主机地址、用户名和密码你不希望每次都输入，你就可以将ENTRYPOINT设置成：ENTRYPOINT mysql -u <用户名> -p <密码> -h <主机名>。而你运行时，只需要指定数据库名。


 
     
> WORKDIR


指令：WORKDIR dir 

用于指定RUN CMD ENTRYPOINT 命令的工作目录。相当于cd命令。

eg:

```
WORKDIR  /srv   将文件目录切换到 /srv
WORKDIR  test    如果当前目录是/srv,则该命令将文件目录切换到 /srv/test/

```

> ADD 

指令：ADD  src  dest   

复制文件 src到容器对应的路径dest。src 可以是文件、文件夹、URL；

```
注意：
   1. 拷贝到到容器的文件和文件夹权限为0755 ，uid 和 gid 为0
   2. 如果文件是tar格式的，拷贝到容器后会自动解压，并删除原文件。
   3. 要ADD的<src>原文件与Dockerfile在同一路径下。
   4. 能ADD远程文件,ADD远程文件必须是：通过远程Dockerfile创建镜像时才可以。
   5. ADD在build镜像时运行一次，运行容器时不再执行。
   6. <dest>必须是绝对路径
 eg：
 ADD htm*   /web/static/  将所有htm开头的文件都添加到/web/static/ 目录下
```

>COPY

 指令：COPY src  dest
 
 作用与ADD相同，只是不能添加URL、以及不能解压tar。
 
 本地源且不为tar文件时推荐使用copy，


>VOLUME

指令：VOLUME ["file","path"]

挂载宿主机文件到指定的容器目录。该指令只能指定容器目录，对应宿主机的目录可以通过docker inspect 查看


```
VOLUME ["/srv/logs/","/srv/config/dbconfig.xml","/srv/dbdata/"]
挂载3个卷，这样的方式会在宿主机上随机创建两个文件目录和一个文件与之相互对应。

如果要指定宿主机目录与挂载卷的映射管理，需要使用 -v 参数

eg:
	docker run -v /home/adrian/data:/data  xxxx
	启动容器时将挂载宿主机目录/home/adrian/data到容器目录/data上.

```


>Dockerfile 不常用知识

1. ARG  创建容器时传入参数
2. ONBUILD 当该镜像被作为基础镜像来创建其它镜像时，执行的一些操作。
3. LABEL
4. STOPSIGNAL
5. HEALTHCHECK
6. SHELL
7. .dockerignore 配置忽略文件

> ONBUILD 


ONBUILD指令用来设置一些触发的指令，用于在当该镜像被作为基础镜像来创建其他镜像时(也就是Dockerfile中的FROM为当前镜像时)执行一些操作，ONBUILD中定义的指令会在用于生成其他镜像的Dockerfile文件的FROM指令之后被执行，上述介绍的任何一个指令都可以用于ONBUILD指令，可以用来执行一些因为环境而变化的操作，使镜像更加通用。

注意：

- ONBUILD中定义的指令在当前镜像的build中不会被执行。
- 可以通过查看docker inspect <image>命令执行结果的OnBuild键来查看某个镜像ONBUILD指令定义的内容。
- ONBUILD中定义的指令会当做引用该镜像的Dockerfile文件的FROM指令的一部分来执行，执行顺序会按ONBUILD定义的先后顺序执行，如果ONBUILD中定义的任何一个指令运行失败，则会使FROM指令中断并导致整个build失败，当所有的ONBUILD中定义的指令成功完成后，会按正常顺序继续执行build。
- ONBUILD中定义的指令不会继承到当前引用的镜像中，也就是当引用ONBUILD的镜像创建完成后将会清除所有引用的ONBUILD指令。
- ONBUILD指令不允许嵌套，例如ONBUILD ONBUILD ADD . /data是不允许的。
- ONBUILD指令不会执行其定义的FROM或MAINTAINER指令。

eg:


```
如果Dockerfile使用以下内容创建镜像 image-1:

...
ONBUILD ADD . /app/src
ONBUILD RUN ehco 'tst'
...


那么，使用 image-1作为基础镜像时，实际上等价于在最后添加了两天指令。

ADD . /app/src
RUN ehco 'tst'


```



> Dockerfile 示例，跑两个web服务到jetty容器


```
FROM jetty:9-jre7

ENV TZ 'Asia/Shanghai'
ENV LANG en_US.UTF-8
ENV LANGUAGE en_US.UTF-8
ENV LC_ALL en_US.UTF-8

RUN mkdir -p /srv/share/logs/vonchersMS/
RUN mkdir -p /srv/share/logs/vouchersAPI/
RUN chmod 777 /srv/share/logs/vouchersAPI/
RUN chmod 777 /srv/share/logs/vonchersMS/

COPY VouchersMS.war /var/lib/jetty/webapps/VouchersMS.war
COPY VouchersAPI.war /var/lib/jetty/webapps/VouchersAPI.war

VOLUME ["/srv/share/logs/vonchersMS/","/srv/share/logs/vouchersAPI/"]

RUN java -jar "$JETTY_HOME/start.jar" --add-to-startd=jmx,stats

```




### 下面是一些同学使用的实践经验

[Dockerfile最佳实践（一）](http://dockone.io/article/131)

[Dockerfile简单说明](http://seanlook.com/2014/11/17/dockerfile-introduction/)

[Docker好多文章](http://dockone.io/people/llitfkitfk)



