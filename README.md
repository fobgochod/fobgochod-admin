# Middleware DMC(Document Management Center)  Help Doc

## MongoDB

### 限制和阈值
- [MongoDB Server Parameters](https://docs.mongodb.com/manual/reference/parameters)
- [MongoDB Limits and Thresholds](https://docs.mongodb.com/manual/reference/limits)

```
# 查看mongodb参数
use admin
db.runCommand({ getParameter: '*' });
```



- 单个document最大16MB
- 数据库(Database)
  - 忽略大小写、
  - 名称不包含(/\. "$*<>:|?)
  - 最长64个字符
- 集合(Collection)
  - 名称下划线或字母开头
  - 不包含($、""、null、不system.开头)
  - 小于255字节(database.collection)***(V4.4+)***
- 字段(Field)
  - 不包含(null、不以$开头)
- 集合最大文档数量
  - 默认无限制
  - 创建集合命令[create](https://docs.mongodb.com/manual/reference/command/create) ，max参数指定，最大2<sup>32</sup>
  - [限制MongoDB中集合中的文档数量](https://www.nhooo.com/note/qa0g47.html)

## GridFS

### GridFS操作（上传、下载）
- [官网关于GridFS操作的说明文档](https://www.it610.com/article/1297225303386562560.htm)

### 分段上传（调用）
- [分片上传和断点续传](https://help.aliyun.com/document_detail/31850.html)
- [Aliyun OSS SDK for Java](https://github.com/aliyun/aliyun-oss-java-sdk)

```maven
<dependency>
    <groupId>com.aliyun.oss</groupId>
    <artifactId>aliyun-sdk-oss</artifactId>
    <version>3.8.0</version>
</dependency>
```

### 分段上传（服务）
- [java 基于 MongoDB GridFS 实现文件服务器](https://my.oschina.net/u/3068023/blog/4917923)




# License
Fobgochod Admin is Open Source software released under the [Apache 2.0 license](https://www.apache.org/licenses/LICENSE-2.0.html).
