# RESTful 笔记


> Spring-boot 快速创建项目

[1分钟创建Spring-boot项目](http://start.spring.io/)

RESTful常用框架：**Jersey**

> RFC JOSN-RFC


全称：Representational State Transfer

>RESTful 概念

如果一个架构符合RESTful的原则，那么久称它为RESTful架构。

### 基于资源（Resources）
	
   资源通常指一类具体是实体，之所以说一类，是因为资源往往是复数的，而不是独一无二的，当然，也存在部分资源就是唯一的。所以，我理解资源即实体。在面向对象的世界里，所有的对象就是资源。
	
   一类或一个资源，可以用一个URI(统一资源识别符)来指向它，这样我们需要与一个资源交互的时候，实际上就是访问一个URI。
	
   在网络的环境下，资源最终都是数据，而数据的外在表现存在很多格式，在HTTP请求中，可以通过Accept 和 Content-Type 来指定数据格式。
参考：[HTTP Content-Type](http://tool.oschina.net/commons)

```
Both, client and server, need to know which format is used for the 
communication. The format has to be specified in the HTTP-Header.

Content-Type defines the request format.
Accept defines a list of acceptable response formats.
```
	
***Content-Type = application/json;charset=UTF-8   不在表格上诉表格中，原因还不明白***


## RESTful 设计原则

### 1. 资源用名词描述，而且用复数
	
实际表现出来就是，资源（URI）用名词来描述，由于大部分资源都是复数的，所以还应该使用复数。

```
	GET /users  					# 获得所有的用户信息
	GET /users/111					# 获得id为111的用户信息  
	GET /users?name=luxiaohu		# 获得名字为luxiaohu的用户信息
	
	示例中users就是资源，而且是复数。当然，如果真的存在独一无二的资源，也是应该用单数的。

```
如果资源有下级资源，则也应该体现在URI上。但这里需要避免URL过长，过长时可以通过传递参数来处理。

```
	GET /users/12/books/12      # 获得12号用户的12号书
	GET /users/12/books			# 获得12号用户的所有书
```

### 2. 使用HTTP请求方式对应资源的操作。

对应关系：

```
   GET		获得资源信息 read
   POST 	新增资源 create
   PUT		更新资源(客户端提供完整的资源) update
   PATCH	更新资源（客户端提供改变的属性）update
   DELETE 	删除资源 delete
   
   
   其中：POST PUT DELETE 都会修改数据状态，如果有参数传递，
   可以使用FORM表单的方式，也可以使用 Request Body的方式，建议使用 Request Body，
   这样统一使用JSON的数据格式。
   
```

### 3. 协议 及 域名

RESTful 本身只是一种规范，所以是支持所有网络传输协议的，通常我们定义API时，都是使用HTTP或者HTTPS。

一个API应该使用专用的域名之下，当然也可以在主域名下。

```
https://api.funguide.com.cn   	# 尽量使用这样的方式
https://funguide.com.cn/api/ 	# 这种方式也是可以的
```

### 4. 使用HTTP Headers 来限制数据格式

用 Content-Type 来定义 请求数据格式
用 Accept 来定义响应数据格式


### 5. 版本（Versioning）

版本号可以放在URL中，也可以放在HTTP头信息中

```
GET https://api.funguide.com.cn/v1/   # 建议使用这种方式
```
### 6. 路径(Endpoint)

路径指API的具体网址。

```
https://api.funguide.com.cn/v1/users    # 用户对应的API地址

```

### 7. 过滤（Filtering）
复杂的查询、排序、分页、指定响应的字段

#### 7.1. 排序 Sorting

```
GET /cars?sort=-manufactorer,+model

或者：

GET /cars?sortby=name,asc：指定返回结果按照哪个属性排序，以及排序顺序。

```

#### 7.2. 指定响应的字段 Field selection


```
GET /cars?fields=manufacturer,model,id,color

```

#### 7.3. 分页 Paging

```
GET /cars?limit=10：指定返回记录的数量
GET /cars?offset=10：指定返回记录的开始位置。
GET /cars?page=2&per_page=100：指定第几页，以及每页的记录数。

下面这种分页是直接响应分页的地址：

Link: <https://blog.mwaysolutions.com/sample/api/v1/cars?offset=15&limit=5>; rel="next",
<https://blog.mwaysolutions.com/sample/api/v1/cars?offset=50&limit=3>; rel="last",
<https://blog.mwaysolutions.com/sample/api/v1/cars?offset=0&limit=5>; rel="first",
<https://blog.mwaysolutions.com/sample/api/v1/cars?offset=5&limit=5>; rel="prev",

```



#### 7.4. 复杂查询 (Filtering)

```
GET /cars?color=red Returns a list of red cars
GET /cars?seats<=2 Returns a list of cars with a maximum of 2 seats
```

### 8. 状态码(Status Codes）
遵循HTTP状态码
参考[HTTP状态码](http://tool.oschina.net/commons?type=5)

Spring框架直接参考：org.springframework.http.HttpStatus.java

```
200 OK - [GET]：服务器成功返回用户请求的数据，该操作是幂等的（Idempotent）。
201 CREATED - [POST/PUT/PATCH]：用户新建或修改数据成功。
202 Accepted - [*]：表示一个请求已经进入后台排队（异步任务）
204 NO CONTENT - [DELETE]：用户删除数据成功。
400 INVALID REQUEST - [POST/PUT/PATCH]：用户发出的请求有错误，服务器没有进行新建或修改数据的操作，该操作是幂等的。
401 Unauthorized - [*]：表示用户没有权限（令牌、用户名、密码错误）。
403 Forbidden - [*] 表示用户得到授权（与401错误相对），但是访问是被禁止的。
404 NOT FOUND - [*]：用户发出的请求针对的是不存在的记录，服务器没有进行操作，该操作是幂等的。
406 Not Acceptable - [GET]：用户请求的格式不可得（比如用户请求JSON格式，但是只有XML格式）。
410 Gone -[GET]：用户请求的资源被永久删除，且不会再得到的。
422 Unprocesable entity - [POST/PUT/PATCH] 当创建一个对象时，发生一个验证错误。
500 INTERNAL SERVER ERROR - [*]：服务器发生错误，用户将无法判断发出的请求是否成功。
```

### 9. 错误处理

```
{
  "errors": [
   {
    "userMessage": "Sorry, the requested resource does not exist",
    "internalMessage": "No car found in the database",
    "code": 34,
    "more info": "http://dev.mwaysolutions.com/blog/api/v1/errors/12345"
   }
  ]
} 

或者

{
    error: "Invalid API key"
}


```


### 10. 返回结果 

```
GET /users			#返回资源对象的列表（数组）
GET /users/1		#返回单个资源对象
POST /users			#返回新生成的资源对象
PUT /users/1		#返回完整的资源对象
PATCH /users/1		#返回完整的资源对象
DELETE /users/1		#返回一个空文档

```

### 11. OAuth2身份认证 及 Hypermedia API

RESTful 身份认证可以使用OAuth2

Hypermedia API 是指在接口返回数据时，提供下一次请求的连接信息（HATEOAS）
***Hypermedia as the Engine of Application State is a principle that hypertext links should be used to create a better navigation through the API.***

最简单的方式就是在API首页提供所有的API访问说明。[参考GitHub](https://api.github.com/)



## 备注
#####参考文档

[blog.mwaysolutions.com](http://blog.mwaysolutions.com/2014/06/05/10-best-practices-for-better-restful-api/)

[www.ruanyifeng.com restful_api](http://www.ruanyifeng.com/blog/2014/05/restful_api.html)

[www.ruanyifeng.com restful](http://www.ruanyifeng.com/blog/2011/09/restful.html)


[RESTful的比较好的总结](http://novoland.github.io/%E8%AE%BE%E8%AE%A1/2015/08/17/Restful%20API%20%E7%9A%84%E8%AE%BE%E8%AE%A1%E8%A7%84%E8%8C%83.html)



