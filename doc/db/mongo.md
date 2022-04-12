# MongoDB

> MongoDB server version: 5.0.6

- [mongo](https://docs.mongodb.com/manual/reference/program/mongo)

```shell
# 导出
mkdir -p /opt/database/mongo
mongodump -d fobgochod -o /opt/database/mongo
# 压缩
cd /opt/database/mongo
tar -czvf fobgochod.tar.gz fobgochod 
```

```shell
# 解压
cd /opt/database/mongo
tar -zxvf fobgochod.tar.gz
# 导入
mongorestore -d fobgochod /opt/database/mongo/fobgochod/
```
