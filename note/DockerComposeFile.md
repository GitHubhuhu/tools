# docker-compose.yml 笔记

docker-compose 可以看做一个服务管理工具，其管理的基本单位就是docker容器。

如果一个服务需要使用多个docker容器时，docker-compose可以很好的管理容器之间的依赖。


[官网地址](https://docs.docker.com/compose/compose-file/)


## 概念

### 服务
   假如需要部署一个项目A，而这个项目需要依赖很多其他的项目，比如依赖nginx、haproxy、缓存、数据库、ngrok等等，如此多的依赖，我们一个一个单独部署会非常麻烦，所以我们需要把他们集中到一起管理， docker-compose就是来完成这个工作，而它管理的这个集合，我就叫他服务。
   
### dokcer-compose.yml 命令

> build


