# Docker 笔记

docker 安装：直接参考 [官网安装](https://www.docker.com/products/docker)

容器：CONTAINER 一个动态可执行的服务器
 
镜像：IMAGE 一个静态文件

[] 说明是数组，可以同时操作多个。

CONTAINER 命令中CONTAINER既可以是id也可以是名字

## 容器的操作命令

>docker info

查看docker基本信息

>docker help

查看帮助文档

	查看单个命令的详细说明：  
	docker COMMAND --help
	docker help COMMAND 

	
>docker run 

运行一个容器

常见参数说明：

	-t 在容器启动一个终端  
	-i 可进行交互
	-d 后台执行  
	-c 要执行的shell，双引号包起来。
	-p 端口映射
	-v 文件目录挂载

使用docker镜像nginx:latest以后台模式启动一个容器,并将容器命名为mynginx。


	docker run --name mynginx -d nginx:latest

使用镜像nginx:latest以后台模式启动一个容器,并将容器的80端口映射到主机随机端口。
	
	docker run -P -d nginx:latest

使用镜像nginx:latest以后台模式启动一个容器,将容器的80端口映射到主机的80端口,主机的目录/data映射到容器的/data。

	docker run -p 主机端口:容器端口 -v 主机目录:容器目录 -d nginx:latest
	docker run -p 80:80 -v /data:/data -d nginx:latest

使用镜像nginx:latest以交互模式启动一个容器,在容器内执行/bin/bash命令。
	
	docker run -it nginx:latest /bin/bash


>docker /stop/start/restart/rm  [OPTIONS] [CONTAINER...]

 停止/启动/重启/移除（容器）

### 运维常用

>docker ps  [OPTIONS]

docker ps 查看正在运行的容器

docker ps -a 查看所有容器

>docker attach CONTAINER

挂载后台运行的容器,直接跟容器进行交互；直观体检就是登陆了容器的服务器

>docker top CONTAINER

显示容器内运行的进程情况

>docker logs CONTAINER

查看运行日志（控制台输出）
	
	查看容器mybusybox从2016年8月1日后的最新20条日志。
	docker logs --since="2016-08-01" --tail=20 mybusybox
	
	

>docker cp CONTAINER:PATH HOSTPATH

把容器内文件复制到主机上

>docker diff CONTAINER

查看容器内文件的变化

>docker inspect [OPTIONS] CONTAINER|IMAGE|TASK [CONTAINER|IMAGE|TASK...]

查看容器运行时详细信息的命令。了解一个Image或者Container的完整构建信息就可以通过这个命令实现。可以同时查看多个

	获取正在运行的容器mybusybox的基本信息，返回json格式
	docker inspect mybusybox

	获取正在运行的容器mymysql的 IP。
	docker inspect -f '{{.NetworkSettings.IPAddress}}' mybusybox


> docker port CONTAINER PRIVATE_PORT

查看Host主机端口与容器暴露出的端口的NAT映射关系
	
	查看主机端口与mybusybox的映射关系
	docker port mybusybox

> docker pause/unpause CONTAINER

暂停和暂停容器

### 不常用命令

>docker events 

查看系统事件--

	显示docker 2016年7月1日后的所有事件。
	docker events  --since="1467302400"

	显示docker 镜像为mysql:5.6 2016年7月1日后的相关事件。
	docker events -f "image"="mysql:5.6" --since="1467302400" 

>docker wait CONTAINER

阻塞对指定容器的其它调用方法，直到容器停止后退进程

>docker kill CONTAINER

杀死容器进程

>docker exec  [OPTIONS] CONTAINER COMMAND [ARG...]

在正在运行的容器上运行一个命令
	
	在mybusybox以交互模式执行/root/runoob.sh脚本
	docker exec -it mybusybox /bin/sh /root/runoob.sh
	
	在mybusybox开启一个交互模式的终端
	docker exec -i -t  mybusybox /bin/bash
	
	-d 后台执行


>docker rename OLD_NAME NEW_NAME  
	 
重命名

>docker version 

查看docker及相关软件版本信息
	 

下面这些命令还不太懂	 

>network 
>
>node 
>
>stats
>
>service 
>
>update
>
>swarm 
>
>volume
>
>create



## 镜像的操作命令

> docker build [OPTIONS] PATH | URL | -


使用URL创建镜像
	
	docker build docker.funguide.com.cn/ngrok-client

通过Dockerfile构建镜像 

	docker build -t jetty/test: .
	ps:注意 Dockerfile 放的位置，默认在当前文件夹下。



> docker tag [OPTIONS] IMAGE[:TAG] [REGISTRYHOST/][USERNAME/]NAME[:TAG]

标记本地镜像，将其归入私有仓库

	docker tag mybusybox docker.funguide.com.cn/mybusybox:v123

标记本地镜像，将其归入某个用户

	docker tag mybusybox luxiaohu/mybusybox:v123
	
标记本地镜像

	docker tag mybusybox mybusybox:v123



>docker commit CONTAINER  <imageName>  

将容器保存为一个镜像（最好创建镜像的方式是使用dockerfile，commit方式作为临时使用）

	docker commit -a "luxiaohu" -m "my ceshi" a404c6c174a2  mybusybox:v134 
	

>docker images 

查看所有的镜像

>docker rmi IMAGE [IMAGE...]

删除镜像

>docker history <imageName>

查看镜像的历史版本

### 导出导入容器或镜像


>export  将容器的文件系统打包成tar包

	导出mysqldb到mysqldb1.tar
	docker export -o mysqldb1.tar mysqldb
	docker export mysqldb > mysqldb.tar

>import 根据tar包文件的内容新建一个镜像
	
	用mysqldb.tar创建镜像mysql:v1
	docker import mysqldb.tar mysql:v1

>save 将镜像保存为tar包文件或者（导出到STDOUT）
	
	保存镜像nginx到文件nginx.tar
	docker save -o nginx.tar nginx
	
>load 从tar文件（或者STDIN）导入一个新的镜像
	
	导入镜像nginx
	docker load -i nginx.tar 
   
 ***PS:export import 操作的是容器，所以可以用来做docker容器迁移，而save 和 load 操作的是镜像***
 
 

## docker 远程仓库相关



默认仓库 docker hub

[创建私有仓库](http://wiki.jikexueyuan.com/project/docker-technology-and-combat/local_repo.html)


>docker login 

登陆

	docker login -u 用户名 -p 密码


>docker logout 


>docker pull [OPTIONS] NAME[:TAG|@DIGEST]

抓取镜像；

	docker pull ubuntu:12.04
	
私有仓库抓取镜像

	docker pull docker.funguide.com.cn/ubuntu:12.04
	
	
	

>docker push  <imageName>

推送镜像
	
	docker push luxiaohu/mybusybox:v123
	
推送到私有仓库
	
	推送私有仓库时需要先打一个tag，然后push上仓库
	docker tag mybusybox docker.funguide.com.cn/mybusybox:v123
	docker push docker.funguide.com.cn/mybusybox:v123
	
	

>docker search <imageName>

检索镜像
	docker search ubuntu:12.04

查看私有仓库里的镜像

	curl https://docker.funguide.com.cn/v1/search
