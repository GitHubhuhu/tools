# docker-compose.yml 笔记

docker-compose 可以看做一个服务管理工具，其管理的基本单位就是docker容器。

如果一个服务需要使用多个docker容器时，docker-compose可以很好的管理容器之间的依赖。


[官网地址](https://docs.docker.com/compose/compose-file/)


## 概念

### 服务
   假如需要部署一个项目A，而这个项目需要依赖很多其他的项目，比如依赖nginx、haproxy、缓存、数据库、ngrok等等，如此多的依赖，我们一个一个单独部署会非常麻烦，所以我们需要把他们集中到一起管理， docker-compose就是来完成这个工作，而它管理的这个集合，我就叫他服务。
   
### dokcer-compose.yml 命令

> ***version2*** 和 ***version1*** 的区别

***version2*** 的 dokcer-compose.yml 开头有 version: '2'

示例

```
version: '2'
services:
  web:
    build: .
    ports:
     - "5000:5000"
    volumes:
     - .:/code
  redis:
    image: redis

```

> build 

构建一个镜像

示例1：

```
build: .

```
以当前目录中 Dokcerfile 构建一个镜像。

示例2：

```
build:
  image: myweb:tag1
  context: ./dir
  dockerfile: Dockerfile-alternate
  args:
    buildno: 1  

```
image 指明需要构建镜像的名称和标识，示例中将构建一个名为myweb,并标签为tag1的镜像。在 ***version1*** 中，build 和 image不能一起出现

context 指明构建镜像是的上下文文件，只能使用相对目录。 ***version2 only***

dockerfile 指明Dockerfile，不指定时默认为 Dokcerfile。

args 设置参数 ，***version2 only*** ,这里设置的参数，在 Dockerfile里可以通过 $key 来引用。

args使用示例：

```

---------docker-componse.yml-------------
build:
  context: .
  args:
    - buildno=1
    - password=secret

-----------------------------------------


---------Dockerfile----------------------

ARG buildno
ARG password

RUN echo "Build number: $buildno"
RUN script-requiring-password.sh "$password"

-----------------------------------------


```


> depends_on

描述两个服务之间的依赖关系。

1. docker-compose up  时，服务将先启动依赖的服务；示例中：db 和 redis 将先启动。
2. docker-compose up SERVICE 时，也会先启动依赖。示例：docker-compose up web 就会先创建并启动 db 和 redis，然后启动 web。

示例：

```
version: '2'
services:
  web:
    build: .
    depends_on:
      - db
      - redis
  redis:
    image: redis
  db:
    image: postgres

```

> dns

指定服务的dns，可以单个，也可以多个。


```
dns: 8.8.8.8

dns:
  - 8.8.8.8
  - 9.9.9.9


```

> dns_search

```
dns_search: example.com

dns_search:
  - dc1.example.com
  - dc2.example.com
```

> tmpfs

挂载一个临时的文件系统到容器,仅早v2使用，可以单个也可以多个。

```
tmpfs: /run

tmpfs:
  - /run
  - /tmp
  
```

> entrypoint 

重写容器入口命令

```
entrypoint:
    - php
    - -d
    - zend_extension=/usr/local/lib/php/extensions/no-debug-non-zts-20100525/xdebug.so
    - -d
    - memory_limit=-1
    - vendor/bin/phpunit
    
```

> environment

设置环境变量,map和list都支持

```
environment:
  RACK_ENV: development
  SHOW: 'true'
  SESSION_SECRET:

environment:
  - RACK_ENV=development
  - SHOW=true
  - SESSION_SECRET

```

> expose

暴露端口,而不是映射到宿主机。内部可访问。

```
expose:
 - "3000"
 - "8000"

```

> external_links

关联 docker-compose.yml之外 容器

关联 非Compose管理的容器。

与`links`语法一致

```
external_links:
 - redis_1
 - project_db_1:mysql
 - project_db_1:postgresql

```

> links

关联容器

```
web:
  links:
   - db
   - db:database
   - redis
```

> image

指定启动容器的镜像。

version1 中 不能与 `build` 同时出现。

```
image: redis
image: ubuntu:14.04
image: tutum/influxdb
image: example-registry.com:4000/postgresql
image: a4bc65fd

```

>ports

暴露端口 [主机端口:容器端口] 或者不指定主机端口。

```
ports:
 - "3000"
 - "3000-3005"
 - "8000:8000"
 - "9090-9091:8080-8081"
 - "49100:22"
 - "127.0.0.1:8001:8001"
 - "127.0.0.1:5000-5010:5000-5010"

```

> volumes volume_driver

volumes 挂载卷。[主机目录:容器目录]，可以不指定主机目录（ps：容器内是可以修改券的访问权限的，像POSTSQL数据库文件挂载到宿主机后，就不允许访问。）RO 是ReadOnly RW是ReadWrite 。

```
volumes:
  # Just specify a path and let the Engine create a volume
  - /var/lib/mysql

  # Specify an absolute path mapping
  - /opt/data:/var/lib/mysql

  # Path on the host, relative to the Compose file
  - ./cache:/tmp/cache

  # User-relative path
  - ~/configs:/etc/configs/:ro

  # Named volume
  - datavolume:/var/lib/mysql

```

volumes有两种声明方式：
1. 在service内声明
2. 在service外部声明，这种声明方式的好处是多个服务可以复用，并方便管理

如果并不是使用的主机目录，则需要用到volume_driver


```

```

> volumes_from

将其他服务或容器的volumes（卷）都挂载到当前服务或容器。默认都是rw模式，可指定ro模式。

```
version2:

volumes_from:
 - service_name
 - service_name:ro
 - container:container_name
 - container:container_name:rw


container只支持V2，所以version1:

volumes_from:
 - service_name
 - service_name:ro
 - container_name
 - container_name:rw

```

> 不常用

```
1. extends  扩展服务
	
	extends:
  	  file: common.yml
  	  service: webapp
  
2. extra_hosts 添加host映射。 与dokcer参数--add-host 一样。
	
	extra_hosts:
 	  - "somehost:162.242.195.82"
 	  - "otherhost:50.31.209.229"

3. labels 标签

4. cpu_shares, cpu_quota, cpuset, domainname, hostname, ipc,
   mac_address, mem_limit, memswap_limit, privileged, read_only,
   restart, shm_size, stdin_open, tty, user, working_dir

	cpu_shares: 73
	cpu_quota: 50000
	cpuset: 0,1
	
	user: postgresql
	working_dir: /code
	
	domainname: foo.com
	hostname: foo
	ipc: host
	mac_address: 02:42:ac:11:65:43
	
	mem_limit: 1000000000
	memswap_limit: 2000000000
	privileged: true  # 特权模式，有时候需要权限的时候需要这个配置
	
	restart: always  # docker 服务重启时，服务重启。重要
	
	read_only: true
	shm_size: 64M
	stdin_open: true
	tty: true
 
```

> 示例2

```
voucher:
    image: voucher
    volumes:
      - /var/tmp/logs:/srv/share/logs
    restart: always

ngrok:
    image: docker.funguide.com.cn/ngrok-client
    links:
      - voucher
    environment:
      SUBDOMAIN: voucher-qa
      PORT: voucher:8080
    restart: always
    
```

```
上述配置一共部署了两个服务，并设置了docker服务重启时，同时重启这两个服务。

```


> 示例1

```
app:
    image: app-image01
    links:
      - db
db:
    image: postgres:9.5
    volumes_from:
      - dbdata
    environment:
    LC_ALL: C.UTF-8
    POSTGRES_USER: app
    POSTGRES_PASSWORD: app
    POSTGRES_DB: app

dbdata:
  image: busybox
  volumes:
    - /var/lib/postgresql

ngrok:
  image: docker.funguide.com.cn/ngrok-client
  links:
    - app
  environment: 
    SUBDOMAIN: app
    PORT: app:8080 
    
```

上述文件一共启动 4 个容器,容器 app 是我们开发运行的应用的容器, db 提供数据库服务,dbdata 提供数据库文件存储，ngrok 是本文的关键配置.
由于 ngrok 和服务器连接的证书编译在二进制文件中，所以只能使用特定的镜像 docker.funguide.com.cn/ngrok-client , 上述配置段中


```
links:
  - app
environment: 
    SUBDOMAIN: app
    PORT: app:8080    
```

ngrok 使用 子域名 app.tuns.io 提供服务 app:8080 的服务.
上述文件配置好之后，用 docker-compose start 就可以启动 4 个容器提供服务，用 ｀http://app.tuns.io就可以访问到app` 容器运行到服务。
