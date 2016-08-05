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

运行一个容器，这个命令好复杂


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

>docker cp CONTAINER:PATH HOSTPATH

把容器内文件复制到主机上

>docker diff CONTAINER

查看容器内文件的变化

>docker inspect [OPTIONS] CONTAINER|IMAGE|TASK [CONTAINER|IMAGE|TASK...]

查看容器运行时详细信息的命令。了解一个Image或者Container的完整构建信息就可以通过这个命令实现。可以同时查看多个

> docker port CONTAINER PRIVATE_PORT

查看Host主机端口与容器暴露出的端口的NAT映射关系

> docker pause/unpause CONTAINER

暂停和暂停容器

### 不常用命令

>docker events 

查看系统运行事件--

>docker wait CONTAINER

阻塞对指定容器的其它调用方法，直到容器停止后退进程

>docker kill CONTAINER

杀死容器进程

>docker exec  [OPTIONS] CONTAINER COMMAND [ARG...]

在正在运行的容器上运行一个命令

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

> docker build


通过Dockerfile构建镜像

> docker tag [OPTIONS] IMAGE[:TAG] [REGISTRYHOST/][USERNAME/]NAME[:TAG]

使用用户名、镜像名、标签名组合 来管理镜像


>docker commit  <容器>  <imageName>  

将容器保存为一个镜像（最好创建镜像的方式是使用dockerfile，commit方式作为临时使用）

>docker images 

查看所有的镜像

>docker rmi IMAGE [IMAGE...]

删除镜像

>docker history <imageName>

查看镜像的历史版本

### 导出导入容器或镜像


>export  将容器的文件系统打包成tar包

	docker export -o mysqldb1.tar mysqldb
	docker export mysqldb > mysqldb.tar

>import 根据tar包文件的内容新建一个镜像
	
	docker import mysqldb.tar mysql:v1

>save 将镜像保存为tar包文件或者（导出到STDOUT）
	
	docker save -o nginx.tar nginx
	
>load 从tar文件（或者STDIN）导入一个新的镜像

   docker load -i xxx-image.tar 
   
 ***PS:export import 操作的是容器，所以可以用来做docker容器迁移，而save 和 load 操作的是镜像***
 
 

## docker 远程仓库相关

>docker login 

登陆

>docker logout 


>docker pull  <imageName>

抓取镜像

>docker push  <imageName>

推送镜像

>docker search <imageName>

检索镜像