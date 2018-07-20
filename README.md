# dk-foundation

#### 项目介绍
spring 快速开发工具包，自动集成了mybatis,sharding-jdbc,redis等

#### 使用说明

1. dk-foundation-common模块为一些基础工具
2. dk-foundation-engine-sharding-jdbc是一个集成了sharding-jdbc的快速开发包
3. dk-foundation-engine-declare-datasource是一个可以通过配置com.dk.foundation.engine.DynamicDataSourcePlugin和@DataSource来决定数据源（@DataSource声明优先级比DynamicDataSourcePlugin优先级高，事务优先级最高）的快速开发包。使用此开发包可以很方便地支持多数据源，方便地开发读写分离的软件服务。

ps:如果你的项目涉及到分库分表，可使用sharding-jdbc包，或选用其它数据库中间件。

#### 快速开发   

添加项目依赖
```
<dependency>
    <groupId>com.dk.foundation</groupId>
    <artifactId>dk-foundation-engine-declare-datasource</artifactId>
    <version>1.0-SNAPSHOT</version>
</dependency>
```

在你的项目启动文件前添加@EnableEngineStart即可，如下所示

```
@MapperScan("com.dk.test.*.mapper")
@EnableEngineStart
public class Startup {

    public static void main(String[] args) throws Exception {
        SpringApplication.run(Startup.class, args);
    }
}
```

**方式一：DynamicDataSourcePlugin** 
```
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE configuration PUBLIC "-//mybatis.org//DTDConfig 3.0//EN" "http://mybatis.org/dtd/mybatis-3-config.dtd">
<configuration>
    <plugins>
        <plugin interceptor="com.dk.foundation.engine.DynamicDataSourcePlugin" />
    </plugins>
</configuration>
```
插件检测到你的sql脚本是插入、更新、删除的语句，自动选择主数据源；如果是查询语句，则随机选择一个读数据源；开启了事务会根据事务选择数据源。

**方式二：@DataSource注解方式** 
```
@DataSource(name="master")
public List<User> selectListBySQL()
{
     List<User> users = userMapper.selectListBySQL();
     return users;
}
```
在使用DynamicDataSourcePlugin同时使用@DataSource注解的时候，会选择注解指定的数据源。
```
@Transactional
@DataSource(name="db_slave_0")
public void insert()
{
    List<User> users = userMapper.selectListBySQL();
    for (User user:users
         ) {
        user.setName(user.getName()+"_testInsert");
        userMapper.insert(user);
    }
}
```
在使用DynamicDataSourcePlugin并使用@DataSource注解的同时，开启了事务，如上代码所示，对于userMapper.selectListBySQL()，DynamicDataSourcePlugin和@DataSource都会选择从数据源，对于userMapper.insert(user)，DynamicDataSourcePlugin选择主数据源，但实际上却都走了主数据源，这是因为开启了事务。
